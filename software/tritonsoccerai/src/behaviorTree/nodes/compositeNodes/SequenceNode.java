package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

// executes nodes in sequence until all completed or until one fails
public class SequenceNode extends CompositeNode {

    private BTNode[] sequence;

    public SequenceNode() {
        super("Sequence Node");
        sequence = new BTNode[0];
    }

    public SequenceNode(String name, BTNode[] sequence) {
        super(name);
        this.sequence = sequence;
    }

    @Override
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
