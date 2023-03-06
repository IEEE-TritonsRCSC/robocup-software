package main.java.core.ai.behaviorTree.nodes.compositeNodes;

import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;

/**
 * Utilized to perform a sequence of tasks
 */
public abstract class SequenceNode extends CompositeNode {

    protected BTNode[] sequence;

    public SequenceNode() {
        super("Sequence Node");
        sequence = new BTNode[0];
    }

    public SequenceNode(String name) {
        super(name);
        sequence = new BTNode[0];
    }

    public SequenceNode(String name, BTNode[] sequence) {
        super(name);
        this.sequence = sequence;
    }

    /**
     * Runs sub-nodes in order until one returns FAILURE or all successfully execute
     */
    @Override
    public NodeState execute() {
        for (BTNode sequenceNode : this.sequence) {
            NodeState completed = sequenceNode.execute();
            if (completed == NodeState.FAILURE) {
                return NodeState.FAILURE;
            }
            if (completed == NodeState.RUNNING) {
                return NodeState.RUNNING;
            }
        }
        return NodeState.SUCCESS;
    }

}
