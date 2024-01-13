package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;
import main.java.core.constants.Team;

import static proto.gc.SslGcRefereeMessage.Referee;

import static main.java.core.util.ProtobufUtils.getPos;

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
        float DISTANCE_CONSTANT = 1;
        // our team was awarded kickoff
        if (((GameInfo.getCurrCommand() == Referee.Command.PREPARE_KICKOFF_BLUE) && (GameInfo.getTeamColor() == Team.BLUE))
            || ((GameInfo.getCurrCommand() == Referee.Command.PREPARE_KICKOFF_YELLOW) && (GameInfo.getTeamColor() == Team.YELLOW))) {
            // robot is closest to ball
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
                while (getPos(GameInfo.getAlly(allyID)).dist(desiredLocation) > DISTANCE_CONSTANT) {
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            // robot is not closest to ball
            else {
                // TODO
                // Move to our side of field
                // Get in formation
                // this.moveToPositionNode.execute();
            }
        }
        // our team was not awarded kickoff
        else {
                // TODO
                // Move to our side of field
                // Get in formation
                // this.moveToPositionNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
