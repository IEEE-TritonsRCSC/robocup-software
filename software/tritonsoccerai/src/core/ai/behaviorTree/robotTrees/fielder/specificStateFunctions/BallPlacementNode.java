package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Ball Placement game state
 */
public class BallPlacementNode extends SequenceNode {

    private final Ally ally;

    public BallPlacementNode(Ally ally) {
        super("Ball Placement Node: " + ally.toString());
        this.ally = ally;
    }

    // TODO

}
