package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Penalty game state
 * If our penalty, approach but do not manipulate ball if closest to ball
 * If our penalty and not closest to ball OR not our penalty,
 * stay 1 m behind ball with a tendency toward middle of field
 */
public class PenaltyNode extends SequenceNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public PenaltyNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Penalty Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    // TODO

}
