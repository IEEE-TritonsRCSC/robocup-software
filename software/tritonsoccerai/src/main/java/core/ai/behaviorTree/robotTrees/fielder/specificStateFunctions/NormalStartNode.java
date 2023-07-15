package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import static proto.triton.FilteredObject.Robot;

/**
 * Handles Normal Start game state
 * Based on previous game state, take appropriate action
 * Check ssl rules for more info
 */
public class NormalStartNode extends CompositeNode {

    private final int allyID;
    private final ClosestToBallNode closestToBallNode;
    private final CoordinatedPassNode coordinatedPassNode;
    private final ShootBallNode shootBallNode;

    public NormalStartNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Normal Start Node: " + allyID);
        this.allyID = allyID;
        this.closestToBallNode = closestToBallNode;
        this.coordinatedPassNode = new CoordinatedPassNode(allyID);
        this.shootBallNode = new ShootBallNode(allyID);
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