package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends TaskNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final PositionSelfNode positionSelfNode;

    public IndirectFreeNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Indirect Free Node: " + ally.toString(), ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(ally);
        this.positionSelfNode = new PositionSelfNode(ally);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall()) {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
                while (this.ally.getPos().dist(desiredLocation) > DISTANCE_CONSTANT) {
                    this.moveToPositionNode.execute(new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2));
                }
            }
            else {
                while (true) {
                    this.positionSelfNode.execute();
                }
            }
        }
        else {
            // TODO - guard foes
        }
        return NodeState.SUCCESS;
    }

}
