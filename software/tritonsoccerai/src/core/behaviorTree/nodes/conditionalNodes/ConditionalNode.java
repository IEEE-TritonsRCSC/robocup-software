package core.behaviorTree.nodes.conditionalNodes;

import core.behaviorTree.nodes.BTNode;
import core.behaviorTree.nodes.NodeState;

// Attach to other nodes and make decisions on whether a branch or a node can be executed
public abstract class ConditionalNode extends BTNode {

    public ConditionalNode() {
        super("Conditional Node");
    }

    public ConditionalNode(String name) {
        super(name);
    }

    @Override
    public NodeState execute() {
        if (conditionSatisfied()) {
            return NodeState.SUCCESS;
        }
        else {
            return NodeState.FAILURE;
        }
    }

    public abstract boolean conditionSatisfied();

}
