package core.behaviorTree;

import core.behaviorTree.nodes.BTNode;
import core.behaviorTree.nodes.compositeNodes.CompositeNode;

public abstract class BehaviorTree {

    protected CompositeNode root;

    public BehaviorTree() {
        this.root = null;
    }

    // initialize nodes within tree
    public abstract void initialize();

    // start execution of tree
    public void execute() {
        root.execute();
    }

}
