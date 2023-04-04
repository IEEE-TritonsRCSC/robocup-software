package main.java.core.module.aiModule;

import main.java.core.ai.behaviorTree.robotTrees.central.CentralCoordinatorRoot;
import main.java.core.constants.ProgramConstants;
import main.java.core.module.Module;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Responsible for coordinating team actions such as passing
 */
public class CentralCoordinatorModule extends Module {

    private final CentralCoordinatorRoot centralCoordinatorRoot;

    public CentralCoordinatorModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        this.centralCoordinatorRoot = new CentralCoordinatorRoot();
    }

    /**
     * At defined frequency, run Central Coordinator Root
     */
    public void execute() {
        this.executor.scheduleAtFixedRate(this.centralCoordinatorRoot, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {

    }

}
