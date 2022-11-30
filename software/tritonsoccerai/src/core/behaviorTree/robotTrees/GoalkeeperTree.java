package core.behaviorTree.robotTrees;

import core.behaviorTree.BehaviorTree;
import core.robot.Ally;

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
