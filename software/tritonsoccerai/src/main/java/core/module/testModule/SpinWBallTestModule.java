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

import static core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RotateInPlaceNode;

import static core.util.ProtobufUtils.getPos;

public class SpinWBallTestModule extends TestModule {
    private final int TEST_ALLY_ID;

    private ChaseBallNode chaseBallNode;
    private RotateInPlaceNode spinNode;
    private boolean running;

    public SpinWBallTestModule(ScheduledThreadPoolExecutor executor) {
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
        this.running = false;
    }

    @Override
    public void run() {
        super.run();

        this.chaseBallNode = new ChaseBallNode(TEST_ALLY_ID);
        this.spinNode = new RotateInPlaceNode(TEST_ALLY_ID);
        this.spinNode.setDribbleOn(true);
        this.running = true;

        goToBall();
        testSpinning();
    }

    private void goToBall() {
        while (!GameInfo.getPossessBall(TEST_ALLY_ID) && this.running) {
            this.chaseBallNode.execute();       
                            
            try {
                TimeUnit.MILLISECONDS.sleep(ProgramConstants.LOOP_DELAY);
            } catch (Exception e) {}
        }
    }

    private void testSpinning() {
        System.out.println("testSpinning started");
        while (this.running) {

            this.spinNode.execute();
                
            try {
                TimeUnit.MILLISECONDS.sleep(ProgramConstants.LOOP_DELAY);
            } catch (Exception e) {}
        }
    }

}