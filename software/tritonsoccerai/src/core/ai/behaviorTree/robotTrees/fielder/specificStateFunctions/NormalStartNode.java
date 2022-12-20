package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Normal Start game state
 */
public class NormalStartNode extends SequenceNode {

    private final Ally ally;

    public NormalStartNode(Ally ally) {
        super("Normal Start Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
