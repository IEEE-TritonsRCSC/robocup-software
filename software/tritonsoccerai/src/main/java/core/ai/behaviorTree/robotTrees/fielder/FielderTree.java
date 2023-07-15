package main.java.core.ai.behaviorTree.robotTrees.fielder;

import main.java.core.ai.behaviorTree.BehaviorTree;
import main.java.core.ai.behaviorTree.robotTrees.fielder.fielderRoot.FielderRootNode;
import static proto.triton.FilteredObject.Robot;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Tree that defines the behavior for fielder allies
 * There will be a separate tree for each ally fielder running concurrently during competition
 */
public class FielderTree extends BehaviorTree {

    private final int fielderID;

    public FielderTree(int fielderID, ScheduledThreadPoolExecutor executor) {
        super();
        this.root = new FielderRootNode(fielderID, executor);
        this.fielderID = fielderID;
    }

}
