package behaviorTree.nodes.compositeNodes;

import behaviorTree.nodes.BTNode;

public class SelectorNode extends CompositeNode {

    private final BTNode[] options;

    public SelectorNode() {
        super("Selector Node");
        this.options = new BTNode[0];
    }

    public SelectorNode(String name, BTNode[] options) {
        super(name);
        this.options = options;
    }

    @Override
    public String execute() {
        for (BTNode optionNode : this.options) {
            String completed = optionNode.execute();
            if (completed == SUCCESS) {
                return SUCCESS;
            }
            if (completed == RUNNING) {
                return RUNNING;
            }
        }
        return FAILURE;
    }

}
