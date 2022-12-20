package core.ai.behaviorTree.robotTrees.goalkeeper;

import core.ai.behaviorTree.BehaviorTree;

/**
 * Tree that defines the behavior for goalkeeper ally
 */
public class GoalkeeperTree extends BehaviorTree {

    public GoalkeeperTree() {
        super();
        this.root = new GoalkeeperRootNode();
    }

}
