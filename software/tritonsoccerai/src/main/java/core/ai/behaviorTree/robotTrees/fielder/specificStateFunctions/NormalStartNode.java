package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.CutPassingLaneNode;

import static proto.triton.FilteredObject.Robot;
import static core.util.ObjectHelper.awardedBall;

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
    private final PositionSelfNode positionSelfNode;
    private final CutPassingLaneNode cutPassingLaneNode;

    public NormalStartNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Normal Start Node: " + allyID);
        this.allyID = allyID;
        this.closestToBallNode = closestToBallNode;
        this.coordinatedPassNode = new CoordinatedPassNode(allyID);
        this.shootBallNode = new ShootBallNode(allyID);
        this.positionSelfNode = new PositionSelfNode(allyID);
        this.cutPassingLaneNode = new CutPassingLaneNode(allyID);
    }

    @Override
    public NodeState execute() {
        if (awardedBall()) {
            switch (GameInfo.getPrevCommand()) {
                case PREPARE_KICKOFF_YELLOW, PREPARE_KICKOFF_BLUE, INDIRECT_FREE_YELLOW, INDIRECT_FREE_BLUE:
                    this.coordinatedPassNode.execute();
                case DIRECT_FREE_YELLOW, DIRECT_FREE_BLUE:
                    // TODO: Depending on whether the other team makes a wall, we compute the probability of scoring, and decide whether to shoot or pass
                    if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                        if (this.shootBallNode.findShot() == null) {
                            this.coordinatedPassNode.execute();
                        }
                        else {
                            this.shootBallNode.execute();
                        }
                    }
                    else {
                        this.positionSelfNode.execute();
                    }
                case PREPARE_PENALTY_YELLOW, PREPARE_PENALTY_BLUE:
                    if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                        this.shootBallNode.execute();
                    }
                    else {
                        this.positionSelfNode.execute();
                    }
            }
        }
        else {
            // We are defending
            this.cutPassingLaneNode.execute();
        }
        return NodeState.SUCCESS;
    }

}