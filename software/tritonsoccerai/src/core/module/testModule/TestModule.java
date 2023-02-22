package core.module.testModule;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import core.module.Module;

public abstract class TestModule extends Module {
    public TestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }
}
