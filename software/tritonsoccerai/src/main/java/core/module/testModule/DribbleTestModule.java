package core.module.testModule;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.GameInfo;
import core.util.Vector2d;
import core.constants.ProgramConstants;
import static core.constants.RobotConstants.*;

import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;

import static core.util.ProtobufUtils.getPos;

public class DribbleTestModule extends TestModule {
    private final int TEST_ALLY_ID;

    private ChaseBallNode chaseBallNode;
    private DribbleBallNode dribbleBallNode;
    private boolean running;
    // private Future<?> future;

    public DribbleTestModule(ScheduledThreadPoolExecutor executor) {
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
        // this.future.cancel(true);
        this.running = false;
    }

    @Override
    public void run() {
        super.run();

        this.chaseBallNode = new ChaseBallNode(TEST_ALLY_ID);
        this.dribbleBallNode = new DribbleBallNode(TEST_ALLY_ID);

        this.running = true;

        goToBall();
        testDribbling();
    }

    private void goToBall() {
        while (!GameInfo.getPossessBall(TEST_ALLY_ID) && this.running) {
            
            this.chaseBallNode.execute();       
                            
            try {
                TimeUnit.MILLISECONDS.sleep(ProgramConstants.LOOP_DELAY);
            } catch (Exception e) {}
        }
    }

    private void testDribbling() {
        Vector2d[] points = {new Vector2d(-2000, 4000), new Vector2d(2000, 4000),
            new Vector2d(-2000, -4000), new Vector2d(2000, -4000)};
        
        int i = 0;
        int ctr = 0;

        // Loop infinitely, going to each of the coordinates in the points array
        while (this.running) {

            this.dribbleBallNode.execute(points[i]);
                
            try {
                TimeUnit.MILLISECONDS.sleep(ProgramConstants.LOOP_DELAY);
                ctr++;
                if (ctr >= (12000 / ProgramConstants.LOOP_DELAY)) {
                    ctr = 0;
                    i = (i + 1) % points.length;
                }
            } catch (Exception e) {}
        }
    }

}