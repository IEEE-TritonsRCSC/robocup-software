package core.ai.behaviorTree.nodes.compositeNodes;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;

// executes nodes in sequence until all completed or until one fails
public abstract class SequenceNode extends CompositeNode {

    private final BTNode[] sequence;

    public SequenceNode() {
        super("Sequence Node");
        sequence = new BTNode[0];
    }

    public SequenceNode(String name, BTNode[] sequence) {
        super(name);
        this.sequence = sequence;
    }

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
