package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Direct Free game state
 */
public class DirectFreeNode extends SequenceNode {

    private final Ally ally;

    public DirectFreeNode(Ally ally) {
        super("Ball Placement Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
