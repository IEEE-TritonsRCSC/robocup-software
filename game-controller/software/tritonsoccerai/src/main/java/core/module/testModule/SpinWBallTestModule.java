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

import static main.java.core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.RotateInPlaceNode;

import static main.java.core.util.ProtobufUtils.getPos;

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