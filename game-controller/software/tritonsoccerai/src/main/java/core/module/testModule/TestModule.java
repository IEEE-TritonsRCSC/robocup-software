package main.java.core.module.testModule;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import main.java.core.module.Module;

public abstract class TestModule extends Module {
    public TestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        System.out.println("In test module: " + executor);
    }
}
