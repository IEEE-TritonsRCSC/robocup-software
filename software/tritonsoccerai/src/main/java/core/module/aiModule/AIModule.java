package main.java.core.module.aiModule;

import main.java.core.module.Module;

import java.io.IOException;
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
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

}
