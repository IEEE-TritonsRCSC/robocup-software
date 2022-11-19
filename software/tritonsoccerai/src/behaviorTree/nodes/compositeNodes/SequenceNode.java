package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

// executes nodes in sequence until all completed or until one fails
public class SequenceNode extends CompositeNode {

    private BTNode[] sequence;

    public SequenceNode() {
        sequence = new BTNode[0];
    }

    public SequenceNode(BTNode[] sequence) {
        this.sequence = sequence;
    }

    public boolean execute() {
        for (BTNode sequenceNode : this.sequence) {
            boolean completed = sequenceNode.execute();
            if (!completed) {
                return false;
            }
        }
        return true;
    }

}
