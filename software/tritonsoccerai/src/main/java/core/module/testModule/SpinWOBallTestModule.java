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

import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.RotateInPlaceNode;

import static main.java.core.util.ProtobufUtils.getPos;

public class SpinWOBallTestModule extends TestModule {
    private final int TEST_ALLY_ID;

    private RotateInPlaceNode spinNode;
    private Future<?> future;

    public SpinWOBallTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        this.TEST_ALLY_ID = GameInfo.getAllyClosestToBall().getId();
    }

    /**
     * Begins repeated periodic execution of MoveToPositionNode
     */
    @Override
    protected void prepare() {}

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

    /**
     * Stops execution of MoveTestModule
     */
    @Override
    public void interrupt() {
        super.interrupt();
        this.future.cancel(true);
    }

    @Override
    public void run() {
        super.run();
        this.spinNode = new RotateInPlaceNode(TEST_ALLY_ID);
        this.future = this.executor.scheduleAtFixedRate(this.spinNode, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
    }

}