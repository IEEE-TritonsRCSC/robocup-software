package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Kickoff game state
 */
public class KickoffNode extends SequenceNode {

    private final Ally ally;

    public KickoffNode(Ally ally) {
        super("Prepare Kickoff Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
