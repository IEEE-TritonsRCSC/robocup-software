package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

// executes nodes in sequence until all completed or until one fails
public class SequenceNode extends CompositeNode {

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
    public String execute() {
        for (BTNode sequenceNode : this.sequence) {
            String completed = sequenceNode.execute();
            if (completed == FAILURE) {
                return FAILURE;
            }
            if (completed == RUNNING) {
                return RUNNING;
            }
        }
        return SUCCESS;
    }

}
