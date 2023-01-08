package core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

/**
 * Defines behavior for goalkeeper when on offense
 * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
 * If goalkeeper has possession, passes ball
 */
public class GKOffenseRootRunnable implements Runnable {

    private final RobotHasPossessionNode havePossession;
    private final CoordinatedPassNode passBall;
    private final GKPositionSelfNode gkPositionSelfNode;

    public GKOffenseRootRunnable() {
        this.havePossession = new RobotHasPossessionNode(GameInfo.getKeeper());
        this.passBall = new CoordinatedPassNode(GameInfo.getKeeper());
        this.gkPositionSelfNode = new GKPositionSelfNode(GameInfo.getKeeper());
    }

    public void run() {
        if(NodeState.isSuccess(this.havePossession.execute())) {
            this.passBall.execute();
        }
        else {
            this.gkPositionSelfNode.execute();
        }
    }

}
