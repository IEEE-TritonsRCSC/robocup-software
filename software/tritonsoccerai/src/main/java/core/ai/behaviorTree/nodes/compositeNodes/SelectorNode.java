package main.java.core.ai.behaviorTree.nodes.compositeNodes;

import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;

/**
 * Utilized to choose the appropriate task to perform
 */
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

    /**
     * Runs sub-nodes in order until one returns SUCCESS or RUNNING
     * Otherwise, returns FAILURE
     */
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
