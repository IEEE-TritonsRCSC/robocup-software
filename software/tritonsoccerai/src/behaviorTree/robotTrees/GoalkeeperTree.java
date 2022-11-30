package behaviorTree.robotTrees;

import behaviorTree.BehaviorTree;
import robot.Ally;

public class GoalkeeperTree extends BehaviorTree {

    private Ally goalkeeper;

    public GoalkeeperTree() {
        super();
    }

    public GoalkeeperTree(Ally goalkeeper) {
        super();
        this.goalkeeper = goalkeeper;
        initialize();
    }

    private void initialize() {
        // TODO
    }

}
