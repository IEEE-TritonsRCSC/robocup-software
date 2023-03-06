package main.java.core.ai.behaviorTree.nodes.compositeNodes;

import main.java.core.ai.behaviorTree.nodes.BTNode;

/**
 * Defines the root of a branch and the rules for how the branch is executed
 */
public abstract class CompositeNode extends BTNode {

    public CompositeNode() {
        super("Composite Node");
    }

    public CompositeNode(String name) {
        super(name);
    }

}
