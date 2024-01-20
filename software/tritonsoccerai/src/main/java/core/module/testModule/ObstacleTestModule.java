package core.module.testModule;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import core.ai.GameInfo;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.constants.ProgramConstants;
import core.util.Vector2d;

public class ObstacleTestModule extends TestModule {

    private MoveToPositionNode moveToPositionNodeAlly;
    private MoveToPositionNode moveToPositionNodeFoe;
    private Future<?> futureAlly;
    private Future<?> futureFoe;

    public ObstacleTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
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
     * Stops execution of ObstacleTestModule
     */
    @Override
    public void interrupt() {
        super.interrupt();
        this.future.cancel(true);
    }

    @Override
    public void run() {
        super.run();
        Vector2d point = new Vector2d(0, 0);
        this.moveToPositionNodeAlly = new MoveToPositionNode(GameInfo.getAllyClosestToBall().getId());
        this.moveToPositionNodeFoe = new MoveToPositionNode(GameInfo.getFoeClosestToBall().getId());
        this.futureAlly = this.executor.scheduleAtFixedRate(this.moveToPositionNodeAlly, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        this.futureFoe = this.executor.scheduleAtFixedRate(this.moveToPositionNodeFoe, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
    }
    
}
