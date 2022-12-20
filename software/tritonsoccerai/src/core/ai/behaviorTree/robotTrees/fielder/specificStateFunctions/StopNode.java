package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Stop game state
 */
public class StopNode extends SequenceNode {

    private final Ally ally;

    public StopNode(Ally ally) {
        super("Stop Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
