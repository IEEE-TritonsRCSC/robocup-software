package core.module.aiModule;

import core.ai.behaviorTree.robotTrees.central.CentralCoordinatorRoot;
import core.constants.ProgramConstants;
import core.module.Module;

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
        this.centralCoordinatorRoot = new CentralCoordinatorRoot(executor);
    }

    /**
     * At defined frequency, run Central Coordinator Root
     */
    public void run() {
        this.centralCoordinatorRoot.execute();
    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {

    }

}
