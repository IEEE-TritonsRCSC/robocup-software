package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends SequenceNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public IndirectFreeNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Indirect Free Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    // TODO

}
