package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Direct Free game state
 */
public class DirectFreeNode extends SequenceNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public DirectFreeNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    // TODO

}
