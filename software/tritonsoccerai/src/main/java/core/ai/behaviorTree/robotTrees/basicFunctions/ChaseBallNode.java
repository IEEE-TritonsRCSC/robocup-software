package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import static proto.triton.FilteredObject.Robot;

import static proto.triton.FilteredObject.Ball;
import static core.util.ProtobufUtils.*;
import core.util.*;

import static core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot.OffenseRootNode;
/**
 * Defines task of chasing ball
 */
public class ChaseBallNode extends TaskNode {

    private final MoveToObjectNode moveToObjectNode;
    private final DribbleBallNode dribbleBallNode;
    // private final double minDistToDribble = 100f; // Change this depending on how close you want the robot before it can dribble

    public ChaseBallNode(int allyID) {
        super("Chase Ball Node: " + allyID, allyID);
        this.moveToObjectNode = new MoveToObjectNode(allyID);
        this.dribbleBallNode = new DribbleBallNode(allyID);
        // this.moveToObjectNode.setDribbleOn(true);
    }

    /**
     * Moves robot to move in direction of ball
     */
    @Override
    public NodeState execute() {
        // System.out.println("Ally " + ally.getId() + " chasing ball");
        this.moveToObjectNode.execute(GameInfo.getBall());
        // System.out.println("Move to pos executed");
        // System.out.println("Running chase ball node");

        // Calculating path from robot to ball
        Ball ball = GameInfo.getBall();
        Robot ally = GameInfo.getAlly(allyID);
        Vector2d allyPos = getPos(ally);
        Vector2d ballPos = getPos(ball);
        Vector2d ballToActor = getPos(ally).sub(ballPos);
        Vector2d offset = ballToActor.project(getVel(ball));
        Vector2d targetPos = ballPos.add(offset);

        // Move to ball
        MoveToPositionNode MoveToPosition = new MoveToPositionNode(allyID);
        MoveToPosition.setDribbleOn(true);
        MoveToPosition.execute(targetPos);

        // DribbleBallNode dribbleBall = new DribbleBallNode(allyID);
        // float distanceFromAllyToBall = allyPos.dist(ballPos);
        
        // Dribble It
        if (getPos(GameInfo.getAlly(allyID)).dist(getPos(GameInfo.getBall())) > DRIBBLE_THRESHOLD){
            dribbleBallNode.execute();
        }
        return NodeState.SUCCESS; 
    }
}
