package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import java.util.ArrayList;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.CutPassingLaneNode;
import static proto.triton.FilteredObject.Robot;
import core.util.Vector2d;

import static core.constants.ProgramConstants.objectConfig;
import static core.constants.ProgramConstants.gameConfig;
import static core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import static core.util.ProtobufUtils.getPos;
import static core.util.ProtobufUtils.getVel;
import static core.util.ObjectHelper.awardedBall;
import static core.util.ObjectHelper.identifyFoeToGuard;

/**
 * Handles Prepare Direct Free game state
 */
public class DirectFreeNode extends TaskNode {

    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final PositionSelfNode positionSelfNode;
    private final CutPassingLaneNode cutPassingLaneNode;
    private final float radius;

    public DirectFreeNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setAvoidBall(true);
        this.positionSelfNode = new PositionSelfNode(allyID);
        this.cutPassingLaneNode = new CutPassingLaneNode(allyID);
        this.radius = objectConfig.robotRadius * 1000f;
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (awardedBall())  {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                if (!GameInfo.getPossessBall(allyID)) {
                    // Robot collides with ball, increasing distance from ball to 1.5*DRIBBLE_THRESHOLD to avoid collision
                    Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 1.5f * DRIBBLE_THRESHOLD - objectConfig.robotRadius);
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            else {
                this.positionSelfNode.execute();
            }
        }
        else {
            // TODO - improve to build wall instead
            buildWall();
        }
        return NodeState.SUCCESS;
    }

    public void buildWall() {
        Vector2d ball = getPos(GameInfo.getBall());

        // Ideally have wall center halfway between ball and goal center
        Vector2d wallCenter = ball.add(GameInfo.getGoalCenter()).scale(0.5f);

        // However wall center might be inside defense area, not allowed, so move it to edge of defense area with 3*robotRadius buffer
        // TO DO: Test this code to make sure it works once ball placement is implemented
        if (wallCenter.isInRect(-1000f - this.radius, -GameInfo.getFieldLength() / 2, 2000f + 2 * this.radius, 1000f + this.radius)) {
            System.out.println("Wall Center inside defense area, moving to edge");
            Vector2d goalToWallCenter = wallCenter.sub(GameInfo.getGoalCenter());
            Vector2d option1 = goalToWallCenter.scale(Math.abs((1000f + 3 * this.radius) / goalToWallCenter.x));
            Vector2d option2 = goalToWallCenter.scale(Math.abs((1000f + 3 * this.radius) / goalToWallCenter.y));
            wallCenter = GameInfo.getGoalCenter().add(option1.mag() < option2.mag() ? option1 : option2);
        }

        // Find vector from goal center to wall center (ie the vector from goal center to ball), and its normal to build wall
        Vector2d goalToWallCenter = wallCenter.sub(GameInfo.getGoalCenter());
        Vector2d normalVector = new Vector2d(-goalToWallCenter.y, goalToWallCenter.x).norm();

        // Find vectors from goal posts to ball
        Vector2d leftPostToBall = ball.sub(GameInfo.getLeftPost());
        Vector2d rightPostToBall = ball.sub(GameInfo.getRightPost());

        // Find left and right boundaries of wall by traveling along normal until we hit line connecting goal post to ball
        Vector2d leftWallBoundary = Vector2d.intersectLines(GameInfo.getLeftPost(), leftPostToBall, wallCenter, normalVector);
        Vector2d rightWallBoundary = Vector2d.intersectLines(GameInfo.getRightPost(), rightPostToBall, wallCenter, normalVector);
        Vector2d wallVector = leftWallBoundary.sub(rightWallBoundary);
        float wallLength = wallVector.mag();

        // get number of robots needed to cover wall length
        int desiredWallSize = (int) Math.ceil(wallLength / (2.0 * this.radius));
        int actualWallSize = Math.min(desiredWallSize, gameConfig.numBots / 2);


        // Debugging
        // System.out.println("Ball: " + ball);
        // System.out.println("Wall Center: " + wallCenter);
        // System.out.println("Normal Vector: " + normalVector);
        // System.out.println("Left Wall Boundary: " + leftWallBoundary);
        // System.out.println("Right Wall Boundary: " + rightWallBoundary);
        // System.out.println("Wall Vector: " + wallVector);  // should have same direction as normal vector
        // System.out.println("Wall Length: " + wallLength);
        // System.out.println("Robot Radius: " + this.radius);
        // System.out.println("Desired Wall Size: " + desiredWallSize);
        // System.out.println("Actual Wall Size: " + actualWallSize);

        // if even number of robots, we want to have positions at center - robotRadius, center + robotRadius, center - 3*robotRadius, center + 3*robotRadius, etc.
        if (allyID <= actualWallSize && actualWallSize % 2 == 0) {
            Vector2d position = wallCenter.add(wallVector.norm().scale(this.radius).scale(allyID % 2 == 0 ? allyID - 1 : -allyID));
            this.moveToPositionNode.execute(position);
            // System.out.println(allyID + " moving to position: " + position);
        }

        // if odd number of robots, we want to have positions at center, center - 2*robotRadius, center + 2*robotRadius, center - 4*robotRadius, center + 4*robotRadius, etc.
        else if (allyID <= actualWallSize && actualWallSize % 2 == 1) {
            Vector2d position = wallCenter.add(wallVector.norm().scale(this.radius).scale(allyID % 2 == 0 ? -allyID : allyID - 1));
            this.moveToPositionNode.execute(position);
            // System.out.println(allyID + " moving to position: " + position);
        }

        // Otherwise, spare robots need to guard passing lanes
        // TO DO: Test this code to make sure it works
        else {
            // Identify all foes with 0.5m of the ball and remove them from foe list
            ArrayList<Robot> foes = new ArrayList<Robot>(GameInfo.getFoeFielders());
            for (Robot foe : GameInfo.getFoeFielders()) {
                if (getPos(foe).dist(ball) < 500f) {
                    foes.remove(foe);
                }
            }
            Robot foeToGuard = identifyFoeToGuard(GameInfo.getAlly(allyID), foes);
            // Copied from MoveToObjectNode, except that avoidBall on moveToPositionNode is set to true here
            float TIME_CONSTANT = 0.5f;
            Vector2d direction = new Vector2d(0.0f, -1.0f * GameInfo.getField().getFieldLength());
            direction = direction.sub(getPos(foeToGuard));
            Vector2d position = getPos(foeToGuard).add(getVel(foeToGuard).scale(TIME_CONSTANT)).add(direction.norm().scale(2.0f * objectConfig.robotRadius));
            this.moveToPositionNode.execute(position);
        }
    }

}
