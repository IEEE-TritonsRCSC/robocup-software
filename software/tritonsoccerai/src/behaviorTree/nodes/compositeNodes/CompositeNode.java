package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

// define the root of a branch and the rules for how the branch is executed
public class CompositeNode extends BTNode {

    public CompositeNode() {
        super("Composite Node");
    }

    public CompositeNode(String name) {
        super(name);
    }

    @Override
    public String execute() {
        // TODO
        return SUCCESS;
    }

}
