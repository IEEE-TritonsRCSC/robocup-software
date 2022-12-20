package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.behaviorTree.BehaviorTree;
import core.fieldObjects.robot.Ally;

/**
 * Tree that defines the behavior for fielder allies
 * There will be a separate tree for each ally fielder running concurrently during competition
 */
public class FielderTree extends BehaviorTree {

    private final Ally fielder;

    public FielderTree(Ally fielder) {
        super();
        this.root = new FielderRootNode(fielder);
        this.fielder = fielder;
    }

}
