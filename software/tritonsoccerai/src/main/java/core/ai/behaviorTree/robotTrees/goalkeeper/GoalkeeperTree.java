package main.java.core.ai.behaviorTree.robotTrees.goalkeeper;

import main.java.core.ai.behaviorTree.BehaviorTree;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.gkRoot.GoalkeeperRootNode;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Tree that defines the behavior for goalkeeper ally
 */
public class GoalkeeperTree extends BehaviorTree {

    public GoalkeeperTree(ScheduledThreadPoolExecutor executor) {
        super();
        this.root = new GoalkeeperRootNode(executor);
    }

}
