package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

/**
 * Defines behavior for goalkeeper when on offense
 * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
 * If goalkeeper has possession, passes ball
 */
public class GKOffenseRootService extends ServiceNode {

    private final RobotHasPossessionNode havePossession;
    private final CoordinatedPassNode passBall;
    private final GKPositionSelfNode gkPositionSelfNode;

    public GKOffenseRootService() {
        super("GK Offense Root Service");
        this.havePossession = new RobotHasPossessionNode(0);
        this.passBall = new CoordinatedPassNode(0);
        this.gkPositionSelfNode = new GKPositionSelfNode(null);
    }

    public NodeState execute() {
        if(NodeState.isSuccess(this.havePossession.execute())) {
            this.passBall.execute();
        }
        else {
            this.gkPositionSelfNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
