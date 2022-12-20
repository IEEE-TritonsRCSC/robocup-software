package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Penalty game state
 */
public class PenaltyNode extends SequenceNode {

    private final Ally ally;

    public PenaltyNode(Ally ally) {
        super("Prepare Penalty Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
