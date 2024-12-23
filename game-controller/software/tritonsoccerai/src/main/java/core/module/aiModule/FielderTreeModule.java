package main.java.core.module.aiModule;

import main.java.core.ai.behaviorTree.robotTrees.fielder.FielderTree;
import static proto.triton.FilteredObject.Robot;
import main.java.core.module.Module;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class FielderTreeModule extends Module {

    private final FielderTree tree;

    public FielderTreeModule(ScheduledThreadPoolExecutor executor, int fielderID) {
        super(executor);
        this.tree = new FielderTree(fielderID, executor);
    }

    @Override
    protected void prepare() {
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

    @Override
    public void run() {
        super.run();
        this.tree.execute();
    }

}
