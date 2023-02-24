package java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootService extends ServiceNode {

    private final BlockBallNode blockBall;

    public GKDefenseRootService() {
        super("GK Defense Root Service");
        this.blockBall = new BlockBallNode(GameInfo.getKeeper());
    }

    /**
     * Always block ball
     */
    @Override
    public NodeState execute() {
        this.blockBall.execute();
        return NodeState.SUCCESS;
    }

}
