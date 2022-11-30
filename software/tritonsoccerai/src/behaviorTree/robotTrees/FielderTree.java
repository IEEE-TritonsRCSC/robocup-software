package behaviorTree.robotTrees;

import behaviorTree.BehaviorTree;
import robot.Ally;

public class FielderTree extends BehaviorTree {

    private Ally[] fielders;

    public FielderTree() {
        super();
        this.fielders = null;
    }

    public FielderTree(Ally[] fielders) {
        super();
        this.fielders = fielders;
    }

    private void initialize() {
        // TODO
    }

}
