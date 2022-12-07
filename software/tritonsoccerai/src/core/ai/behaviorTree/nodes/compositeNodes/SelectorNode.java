package core.ai.behaviorTree.nodes.compositeNodes;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;

public abstract class SelectorNode extends CompositeNode {

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
    public NodeState execute() {
        for (BTNode optionNode : this.options) {
            NodeState completed = optionNode.execute();
            if (completed == NodeState.SUCCESS) {
                return NodeState.SUCCESS;
            }
            if (completed == NodeState.RUNNING) {
                return NodeState.RUNNING;
            }
        }
        return NodeState.FAILURE;
    }

}
