package java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.GameState;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import java.core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import static proto.triton.FilteredObject.Robot;

/**
 * Handles Normal Start game state
 * Based on previous game state, take appropriate action
 * Check ssl rules for more info
 */
public class NormalStartNode extends CompositeNode {

    private final Robot ally;
    private final ClosestToBallNode closestToBallNode;
    private final CoordinatedPassNode coordinatedPassNode;
    private final ShootBallNode shootBallNode;

    public NormalStartNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Normal Start Node: " + ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.coordinatedPassNode = new CoordinatedPassNode(ally);
        this.shootBallNode = new ShootBallNode(ally);
    }

    @Override
    public NodeState execute() {
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            switch (GameInfo.getPrevState()) {
                case PREPARE_KICKOFF, PREPARE_INDIRECT_FREE, PREPARE_DIRECT_FREE -> this.coordinatedPassNode.execute();
                case PREPARE_PENALTY -> this.shootBallNode.execute();
            }
        }
        return NodeState.SUCCESS;
    }

}