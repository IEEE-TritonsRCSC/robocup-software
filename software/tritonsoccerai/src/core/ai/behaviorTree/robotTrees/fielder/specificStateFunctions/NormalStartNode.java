package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Normal Start game state
 * Based on previous game state, take appropriate action
 * Check ssl rules for more info
 */
public class NormalStartNode extends CompositeNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;
    private final CoordinatedPassNode coordinatedPassNode;
    private final ShootBallNode shootBallNode;

    public NormalStartNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Normal Start Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.coordinatedPassNode = new CoordinatedPassNode(ally);
        this.shootBallNode = new ShootBallNode(ally);
    }

    @Override
    public NodeState execute() {
        return NodeState.SUCCESS;
    }

    public NodeState execute(GameState prevState) {
        // TODO
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            switch (prevState) {
                case PREPARE_KICKOFF, PREPARE_INDIRECT_FREE, PREPARE_DIRECT_FREE -> this.coordinatedPassNode.execute();
                case PREPARE_PENALTY -> this.shootBallNode.execute();
            }
        }
        return NodeState.SUCCESS;
    }

}