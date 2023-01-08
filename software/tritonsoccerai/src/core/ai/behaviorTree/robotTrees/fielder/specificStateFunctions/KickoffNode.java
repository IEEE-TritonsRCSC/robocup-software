package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Prepare Kickoff game state
 * Moves robot to our side of field
 * If our kickoff, moves into center circle if closest to ball
 */
public class KickoffNode extends SequenceNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public KickoffNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Kickoff Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    // TODO

}
