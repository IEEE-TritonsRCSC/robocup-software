package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;

    public BallPlacementNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + ally.toString(), ally);
        this.ally = ally;
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
            while (GameInfo.getBall().getPos().dist(GameInfo.getBallPlacementLocation()) > DISTANCE_CONSTANT) {
                this.dribbleBallNode.execute(GameInfo.getBallPlacementLocation());
            }
        }
        else {
            // TODO - move away from placement location if close to it
        }
        return NodeState.SUCCESS;
    }

}
