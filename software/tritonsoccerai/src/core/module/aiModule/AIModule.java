package core.module.aiModule;

import core.ai.GameInfo;
import core.ai.behaviorTree.robotTrees.fielder.FielderTree;
import core.ai.behaviorTree.robotTrees.goalkeeper.GoalkeeperTree;
import core.fieldObjects.robot.Ally;
import core.module.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * AI Module - refer to Figure 1
 * Houses all game-related info
 */
public class AIModule extends Module {

    public AIModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
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
