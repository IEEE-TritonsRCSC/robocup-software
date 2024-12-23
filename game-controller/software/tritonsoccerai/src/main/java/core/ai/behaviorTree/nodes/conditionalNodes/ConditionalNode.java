package main.java.core.ai.behaviorTree.nodes.conditionalNodes;

import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;

/**
 * Attaches to other nodes to help makes decisions on whether a branch or node can/should
 * be executed
 */
public abstract class ConditionalNode extends BTNode {

    public ConditionalNode() {
        super("Conditional Node");
    }

    public ConditionalNode(String name) {
        super(name);
    }

    /**
     * Checks if condition satisfied
     */
    @Override
    public NodeState execute() {
        if (conditionSatisfied()) {
            return NodeState.SUCCESS;
        }
        else {
            return NodeState.FAILURE;
        }
    }

    /**
     * Defines condition to check
     * Must be overriden in subclasses
     */
    public abstract boolean conditionSatisfied();

}
