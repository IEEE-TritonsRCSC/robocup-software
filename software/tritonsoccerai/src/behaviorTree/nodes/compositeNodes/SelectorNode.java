package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

public class SelectorNode extends CompositeNode {

    private BTNode[] options;

    public SelectorNode() {
        super("Selector Node");
        this.options = new BTNode[0];
    }

    public SelectorNode(String name, BTNode[] options) {
        super(name);
        this.options = options;
    }

    @Override
    public boolean execute() {

    }

}
