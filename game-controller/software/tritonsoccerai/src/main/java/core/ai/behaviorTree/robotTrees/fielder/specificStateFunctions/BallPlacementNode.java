package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import static proto.triton.FilteredObject.Robot;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ObjectHelper.awardedBall;
import main.java.core.util.Vector2d;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {
    
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final ChaseBallNode chaseBallNode;

    public BallPlacementNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.dribbleBallNode = new DribbleBallNode(allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.chaseBallNode = new ChaseBallNode(allyID);
    }

    /**
     * If our possession AND robot closest to ball, dribble ball to placement location
     * Otherwise, move away from placement location
     */
    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 100;
        if (awardedBall() && (closestToBallNode.execute() == NodeState.SUCCESS)) {
            if (!GameInfo.getPossessBall(allyID)) {
                this.chaseBallNode.execute();
            }
            else {
                this.dribbleBallNode.execute(GameInfo.getBallPlacementLocation());
            }
        }
        else {
            // move away from placement location if close to it
            Robot ally = GameInfo.getAlly(allyID);
            Vector2d allyPos = getPos(ally);
            Vector2d ballPos = GameInfo.getBallPlacementLocation();
            // System.out.println("ballposition:" + ballPos);
            // System.out.println("allyposition:" + allyPos);
            Vector2d ballToAlly = getPos(ally).sub(ballPos);
            // System.out.println("Dist:" + ballToAlly);
            if (ballToAlly.mag() < 2000) {
                Vector2d targetPos =  ballPos.add(new Vector2d(10 * (allyPos.x - ballPos.x), 10 * (allyPos.y - ballPos.y)));
                // System.out.println("targetPos: " + targetPos);
                // MoveToPositionNode MoveToPosition = new MoveToPositionNode(allyID);
                // MoveToPosition.execute(targetPos);
                this.moveToPositionNode.execute(targetPos);
            }
           
        }
        return NodeState.SUCCESS;
    }

}
