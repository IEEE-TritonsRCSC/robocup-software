package core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

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
        this.gkPositionSelfNode = new GKPositionSelfNode();
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
