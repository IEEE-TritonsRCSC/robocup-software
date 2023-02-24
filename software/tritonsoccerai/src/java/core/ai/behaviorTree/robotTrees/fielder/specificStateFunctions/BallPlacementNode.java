package java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import static proto.triton.FilteredObject.Robot;

import static java.core.util.ProtobufUtils.getPos;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {
    
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;

    public BallPlacementNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + ally, ally);
        this.closestToBallNode = closestToBallNode;
        this.dribbleBallNode = new DribbleBallNode(ally);
    }

    /**
     * If our possession AND robot closest to ball, dribble ball to placement location
     * Otherwise, move away from placement location
     */
    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            while (getPos(GameInfo.getBall()).dist(GameInfo.getBallPlacementLocation()) > DISTANCE_CONSTANT) {
                this.dribbleBallNode.execute(GameInfo.getBallPlacementLocation());
            }
        }
        else {
            // TODO - move away from placement location if close to it
        }
        return NodeState.SUCCESS;
    }

}
