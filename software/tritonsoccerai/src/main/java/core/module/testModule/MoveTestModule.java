package main.java.core.module.testModule;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class MoveTestModule extends TestModule {
    private static final int TEST_ALLY_ID = 1;

    public MoveTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {

    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        super.run();
    }
}
