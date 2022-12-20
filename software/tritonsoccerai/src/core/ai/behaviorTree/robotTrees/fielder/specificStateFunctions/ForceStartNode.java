package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Force Start game state
 */
public class ForceStartNode extends SequenceNode {

    private final Ally ally;

    public ForceStartNode(Ally ally) {
        super("Force Start Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
