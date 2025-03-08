package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import static proto.triton.FilteredObject.Robot;

import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.awardedBall;
import core.util.Vector2d;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {
    
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final ChaseBallNode chaseBallNode;
    private final float DISTANCE_CONSTANT = 1000;

    public BallPlacementNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.dribbleBallNode = new DribbleBallNode(allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setAvoidBall(true);
        this.chaseBallNode = new ChaseBallNode(allyID);
    }

    /**
     * Executes the BallPlacementNode behavior.
     * 
     * This method performs the following steps:
     * 1. Checks if the ball is awarded and if the current robot is the closest to the ball.
     * 2. Retrieves the positions of the robot, the final target location, and the ball.
     * 3. Calculates the direction vector from the ball to the target location.
     * 4. If the robot is close enough to the target location, it returns SUCCESS.
     * 5. If the robot does not possess the ball:
     *    - Moves the robot to a position "behind" the ball relative to the target location.
     * 6. If the robot possesses the ball:
     *    - Moves the ball towards the target location.
     * 7. If the ball is not awarded or the current robot is not the closest to the ball:
     *    - Moves the robot away from the placement location if it is too close.
     * 
     * @return NodeState.SUCCESS if the behavior is executed successfully.
     */
    @Override
    public NodeState execute() {
        Vector2d finalPos = GameInfo.getBallPlacementLocation();
        if (awardedBall() && (closestToBallNode.execute() == NodeState.SUCCESS)) {
            // Locations of the ball and the robot
            Vector2d robotPos = getPos(GameInfo.getAlly(allyID));
            Vector2d ballPos = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY());

            // Ball to Target Vector
            Vector2d moveDirection = finalPos.sub(ballPos);
            Vector2d moveDirectionUnit = moveDirection.norm();
            
            // If robot is close enough to the target location
            if (moveDirection.mag() < 50) {
                System.out.println("I DID IT!!!!");
                return NodeState.SUCCESS;
            }

            if (!GameInfo.getPossessBall(allyID)) {
                // Stage 1: Move "behind" the ball
                float distanceToBall = robotPos.dist(ballPos);
                float scaleFactor = Math.min(distanceToBall * 0.5f, DISTANCE_CONSTANT);
                Vector2d targetRobotPos = ballPos.sub(moveDirectionUnit.scale(scaleFactor));
                
                System.out.println("Distance to ball (should be decreasing): " + distanceToBall);
                this.moveToPositionNode.execute(targetRobotPos);
            }
            else {
                // Stage 2: Move ball to the target location
                float scaleFactor = Math.min(moveDirection.mag() * 0.5f, DISTANCE_CONSTANT);
                Vector2d targetRobotPos = robotPos.add(moveDirectionUnit.scale(scaleFactor));

                System.out.println("Distance from ball to target (should be decreasing): " + moveDirection.mag());
                this.dribbleBallNode.execute(targetRobotPos);
            }
        }
        else {
            // move away from placement location if close to it
            Robot ally = GameInfo.getAlly(allyID);
            Vector2d allyPos = getPos(ally);
            // System.out.println("ballposition:" + ballPos);
            // System.out.println("allyposition:" + allyPos);
            Vector2d ballToAlly = getPos(ally).sub(finalPos);
            // System.out.println("Dist:" + ballToAlly);
            if (ballToAlly.mag() < 2000) {
                Vector2d targetPos =  finalPos.add(new Vector2d(10 * (allyPos.x - finalPos.x), 10 * (allyPos.y - finalPos.y)));
                // System.out.println("targetPos: " + targetPos);
                // MoveToPositionNode MoveToPosition = new MoveToPositionNode(allyID);
                // MoveToPosition.execute(targetPos);
                this.moveToPositionNode.execute(targetPos);
            }
        }
        return NodeState.SUCCESS;
    }

}