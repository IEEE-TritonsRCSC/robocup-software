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

    private final ScheduledThreadPoolExecutor executor;
    private final CentralCoordinatorRoot centralCoordinatorRoot;

    public CentralCoordinatorModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        this.executor = executor;
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
