package main.java.core.module.testModule;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.constants.ProgramConstants;
import main.java.core.util.Vector2d;

public class ObstacleTestModule extends TestModule {

    private MoveToPositionNode moveToPositionNode1;
    private MoveToPositionNode moveToPositionNode2;
    private Future<?> future1;
    private Future<?> future2;

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
        this.future1.cancel(true);
        this.future2.cancel(true);
    }

    @Override
    public void run() {
        super.run();
        this.moveToPositionNode1 = new MoveToPositionNode(0);
        this.moveToPositionNode2 = new MoveToPositionNode(1);

        Vector2d[] points = {new Vector2d(-2000, 4000), new Vector2d(2000, 4000),
            new Vector2d(-2000, -4000), new Vector2d(2000, -4000)};
        
        while (true) {
            for (Vector2d point : points) {
                this.moveToPositionNode1.setTargetLocation(point);
                this.moveToPositionNode2.setTargetLocation(point);

                this.future1 = this.executor.scheduleAtFixedRate(this.moveToPositionNode1, ProgramConstants.INITIAL_DELAY,
                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.future2 = this.executor.scheduleAtFixedRate(this.moveToPositionNode2, ProgramConstants.INITIAL_DELAY,
                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                    
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {}
            }
        }
    }
}
