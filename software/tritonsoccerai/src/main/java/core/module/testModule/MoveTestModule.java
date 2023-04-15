package main.java.core.module.testModule;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.GameInfo;
import main.java.core.util.Vector2d;
import main.java.core.constants.ProgramConstants;

public class MoveTestModule extends TestModule {
    private final int TEST_ALLY_ID;

    private final MoveToPositionNode moveToPositionNode = new MoveToPositionNode(GameInfo.getAllyClosestToBall());
    private Future<?> future;

    public MoveTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        System.out.println("In MoveTestModule: " + executor);
        this.TEST_ALLY_ID = GameInfo.getAllyClosestToBall().getId();
    }

    @Override
    protected void prepare() {
        Vector2d point = new Vector2d(0, 0);
        
        this.future = this.executor.scheduleAtFixedRate(moveToPositionNode, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.future.cancel(true);
    }

    @Override
    public void run() {
        super.run();
    }

}
