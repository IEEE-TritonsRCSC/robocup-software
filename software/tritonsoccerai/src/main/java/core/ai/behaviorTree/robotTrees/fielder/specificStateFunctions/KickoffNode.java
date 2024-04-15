package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;

import static main.java.core.constants.ProgramConstants.objectConfig;
import static main.java.core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import static main.java.core.constants.ProgramConstants.gameConfig;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ObjectHelper.awardedBall;

/**
 * Handles Prepare Kickoff game state
 * Moves robot to our side of field
 * If our kickoff, moves into center circle if closest to ball
 */
public class KickoffNode extends TaskNode {

    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private static final Vector2d defaultPosition = new Vector2d(
        0, GameInfo.getField().getFieldLength() / 4);

    public KickoffNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Prepare Kickoff Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
    }

    @Override
    public NodeState execute() {
        // float DISTANCE_CONSTANT = 100;
        // our team was awarded kickoff
        if (awardedBall()) {
            // robot is closest to ball
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                if (!GameInfo.getPossessBall(allyID)) {
                    Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - DRIBBLE_THRESHOLD - objectConfig.robotRadius);
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            // robot is not closest to ball
            else {
                // TODO
                // Move to our side of field
                // Get in formation
                // this.moveToPositionNode.execute();
                Vector2d location = new Vector2d((allyID - (gameConfig.numBots / 2)) * 1400f, GameInfo.getField().getFieldLength() / -4);
                this.moveToPositionNode.execute(location);
            }
        }
        // our team was not awarded kickoff
        else {
                // TODO
                // Move to our side of field
                // Get in formation
                // this.moveToPositionNode.execute();
                Vector2d location = new Vector2d((allyID - (gameConfig.numBots / 2)) * 600f, GameInfo.getField().getFieldLength() / -10);
                this.moveToPositionNode.execute(location);
        }
        return NodeState.SUCCESS;
    }

}
