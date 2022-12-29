package core.module.aiModule;

import core.ai.behaviorTree.robotTrees.fielder.FielderTree;
import core.module.Module;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class FielderTreeModule extends Module {

    // TODO

    private FielderTree tree;

    public FielderTreeModule(ScheduledThreadPoolExecutor executor, FielderTree tree) {
        super(executor);
        this.tree = tree;
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
