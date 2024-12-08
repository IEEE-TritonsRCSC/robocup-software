package main.java.core.module.testModule;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;

import static proto.triton.FilteredObject.*;
// import java.lang.Math;

import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
// import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.constants.ProgramConstants;
import main.java.core.module.testModule.TestModule;
import main.java.core.util.Vector2d;
import proto.triton.FilteredObject.Robot;
import main.java.core.ai.GameInfo;
// import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
// import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.RotateBotNode;

public class PositionTestModule extends TestModule{
    private final int TEST_ALLY_ID;
    private PositionSelfNode PositionBot;

    private Future<?> future;
    private float kP = 0.6f; 
    private Robot ally;
    private MoveToObjectNode moveToObjectNode;
    private Future<?> moveFuture;


    public PositionTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        // this.TEST_ALLY_ID = GameInfo.getAllyClosestToBall().getId();
        this.TEST_ALLY_ID = 0;
        this.ally = GameInfo.getAlly(this.TEST_ALLY_ID);
        System.out.println("ALLY ID: " + this.TEST_ALLY_ID);
    }

    /**
     * Begins repeated periodic execution of CoordinatedPassTestModule
     */
    @Override
    protected void prepare() {}

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {

    }

    /**
     * Stops execution of CoordinatedPassTestModule
     */
    @Override
    public void interrupt() {
        super.interrupt();
        this.future.cancel(true);
    }

    @Override
    public void run() {
        super.run();
        this.PositionBot = new PositionSelfNode(this.TEST_ALLY_ID);

        while(true){
            this.PositionBot.execute();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (Exception e) {}
        }

    }

        // if(distance(robot_x, robot_y, ball_x, ball_y) > 1000){
        //     this.moveToObjectNode = new MoveToObjectNode(GameInfo.getAllyClosestToBall().getId());
        //     this.moveFuture = this.executor.scheduleAtFixedRate(this.moveToObjectNode, ProgramConstants.INITIAL_DELAY,
        //                                 ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        // }else{
        //     this.coordinatedPassNode = new CoordinatedPassNode(GameInfo.getAllyClosestToBall().getId());
        //     this.coordinatedPassFuture = this.executor.scheduleAtFixedRate(this.coordinatedPassNode, ProgramConstants.INITIAL_DELAY,
        //                                 ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        // }
    

    private float distance(float x1, float y1, float x2, float y2){
        return (float) Math.pow((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2), 0.5);
    }
}