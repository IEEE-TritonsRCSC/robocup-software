package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Normal Start game state
 * Based on previous game state, take appropriate action
 * Check ssl rules for more info
 */
public class NormalStartNode extends CompositeNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;

    public NormalStartNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Normal Start Node: " + ally.toString());
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
    }

    @Override
    public NodeState execute() {
        return NodeState.SUCCESS;
    }

    public NodeState execute(GameState prevState) {
        // TODO
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            switch (prevState) {
                case PREPARE_KICKOFF:
                case PREPARE_INDIRECT_FREE:
                    // execute coordinated pass
                    break;
                case PREPARE_DIRECT_FREE:
                    // execute offense root node
                    break;
                case PREPARE_PENALTY:
                    // execute shoot ball node
                    break;
            }
        }
        else {
            while (true) {
                // do nothing, just wait for game state change
            }
        }
        return NodeState.SUCCESS;
    }

}