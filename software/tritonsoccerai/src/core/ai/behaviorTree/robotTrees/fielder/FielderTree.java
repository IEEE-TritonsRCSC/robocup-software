package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.behaviorTree.BehaviorTree;
import core.fieldObjects.robot.Ally;

public class FielderTree extends BehaviorTree {

    private Ally fielder;

    public FielderTree(Ally fielder) {
        super();
        this.root = new FielderRootNode(fielder);
        this.fielder = fielder;
    }

}
