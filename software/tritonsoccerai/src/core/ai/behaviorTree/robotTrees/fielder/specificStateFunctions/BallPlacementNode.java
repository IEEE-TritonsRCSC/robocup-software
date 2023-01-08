package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends SequenceNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public BallPlacementNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    // TODO

}
