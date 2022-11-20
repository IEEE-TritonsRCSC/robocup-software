package behaviorTree.nodes;

// Attach to other nodes and make decisions on whether a branch or a node can be executed
public class ConditionalNode extends BTNode {

    public ConditionalNode() {
        super("Conditional Node");
    }

    public ConditionalNode(String name) {
        super(name);
    }

    @Override
    public boolean execute() {

    }

}
