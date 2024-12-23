package main.java.core.ai.behaviorTree;

import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;

/**
 * Superclass that defines behavior trees
 */
public abstract class BehaviorTree {

    protected CompositeNode root;

    public BehaviorTree() {
        this.root = null;
    }

    // start execution of tree
    public void execute() {
        root.execute();
    }

}
