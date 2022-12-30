package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootNode extends CompositeNode {

    // TODO
    private final BlockBallNode blockballnode;

    public GKDefenseRootNode() {
        super("GKDefense Root");
        this.blockballnode = new BlockBallNode();
    }

    /*
     * Always block ball
     */
    @Override
    public NodeState execute() {
        this.blockballnode.execute();
        return NodeState.SUCCESS;
    }

}
