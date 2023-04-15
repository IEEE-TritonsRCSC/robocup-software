package main.java.core.module.aiModule;

import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.GoalkeeperTree;
import main.java.core.module.Module;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class GKTreeModule extends Module {

    private final GoalkeeperTree tree;

    public GKTreeModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        this.tree = new GoalkeeperTree(executor);
    }

    @Override
    protected void prepare() {
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

}
