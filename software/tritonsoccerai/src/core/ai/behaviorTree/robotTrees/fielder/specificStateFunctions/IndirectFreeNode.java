package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends SequenceNode {

    private final Ally ally;

    public IndirectFreeNode(Ally ally) {
        super("Prepare Indirect Free Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
