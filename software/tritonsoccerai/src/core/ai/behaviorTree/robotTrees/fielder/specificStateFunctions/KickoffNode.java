package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import proto.filtered_object.Robot;
import core.util.Vector2d;

import static core.util.ProtobufUtils.getPos;

/**
 * Handles Prepare Kickoff game state
 * Moves robot to our side of field
 * If our kickoff, moves into center circle if closest to ball
 */
public class KickoffNode extends TaskNode {

    private final Robot ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public KickoffNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Kickoff Node: " + ally, ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
            while (getPos(this.ally).dist(desiredLocation) > DISTANCE_CONSTANT) {
                this.moveToPositionNode.execute(desiredLocation);
            }
        }
        else {
            // TODO - move to our side of field
        }
        return NodeState.SUCCESS;
    }

}
