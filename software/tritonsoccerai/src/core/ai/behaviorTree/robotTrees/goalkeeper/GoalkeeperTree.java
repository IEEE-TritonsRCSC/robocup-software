package core.ai.behaviorTree.robotTrees.goalkeeper;

import core.ai.behaviorTree.BehaviorTree;

public class GoalkeeperTree extends BehaviorTree {

    public GoalkeeperTree() {
        super();
        this.root = new GoalkeeperRootNode();
    }

}
