package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootNode extends CompositeNode {

    private final TaskNode blockBall;

    public GKDefenseRootNode() {
        super("GK Defense Node");
        this.blockBall = new BlockBallNode();
    }

    /*
     * Always block ball
     */
    @Override
    public NodeState execute() {
        return this.blockBall.execute();
    }

}
