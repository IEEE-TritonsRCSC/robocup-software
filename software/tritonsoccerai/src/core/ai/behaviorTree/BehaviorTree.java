package core.ai.behaviorTree;

import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;

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
