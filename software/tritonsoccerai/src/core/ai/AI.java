package core.ai;

import core.ai.behaviorTree.robotTrees.fielder.FielderTree;
import core.ai.behaviorTree.robotTrees.goalkeeper.GoalkeeperTree;
import core.fieldObjects.robot.Ally;
import core.module.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class AI extends Module {

    private ArrayList<FielderTree> fielderTrees;
    private GoalkeeperTree goalkeeperTree;

    public AI(ScheduledThreadPoolExecutor executor) {
        // TODO
        super(executor);
        // initialize GameInfo
        // create channels for each ally
        this.fielderTrees = new ArrayList<FielderTree>();
        for (Ally fielder : GameInfo.getFielders()) {
            fielderTrees.add(new FielderTree(fielder));
        }
        this.goalkeeperTree = new GoalkeeperTree();
        for (FielderTree tree : this.fielderTrees) {
            tree.execute();
        }
        goalkeeperTree.execute();
    }

    @Override
    protected void prepare() {
        // TODO
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        // TODO
    }

}
