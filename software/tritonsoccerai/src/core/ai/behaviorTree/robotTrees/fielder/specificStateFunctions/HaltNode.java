package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Halt game state
 */
public class HaltNode extends SequenceNode {

    private final Ally ally;

    public HaltNode(Ally ally) {
        super("Halt Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
