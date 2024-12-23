package core.module.testModule;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;

import static proto.triton.FilteredObject.*;
// import java.lang.Math;

import core.ai.behaviorTree.robotTrees.basicFunctions.RotateBotNode;
// import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.constants.ProgramConstants;
import core.module.testModule.TestModule;
import core.util.Vector2d;
import proto.triton.FilteredObject.Robot;
import core.ai.GameInfo;
// import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
// import core.ai.behaviorTree.robotTrees.basicFunctions.RotateBotNode;

public class RotateBotTestModule extends TestModule{
    private final int TEST_ALLY_ID;
    private RotateBotNode RotateBot;

    private Future<?> future;
    private float kP = 0.6f; 
    private Robot ally;
    private MoveToObjectNode moveToObjectNode;
    private Future<?> moveFuture;


    public RotateBotTestModule(ScheduledThreadPoolExecutor executor) {
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
        
        // Initialize RotateBotNode with TEST_ALLY_ID
        this.RotateBot = new RotateBotNode(this.TEST_ALLY_ID);

        float robot_x = GameInfo.getAllyClosestToBall().getX(), robot_y = GameInfo.getAllyClosestToBall().getY();
        float ball_x = GameInfo.getBall().getX(), ball_y = GameInfo.getBall().getY();

        this.moveToObjectNode = new MoveToObjectNode(GameInfo.getAllyClosestToBall().getId());

        while(distance(robot_x, robot_y, ball_x, ball_y) > 500){

            robot_x = GameInfo.getAllyClosestToBall().getX();
            robot_y = GameInfo.getAllyClosestToBall().getY();
            ball_x = GameInfo.getBall().getX();
            ball_y = GameInfo.getBall().getY();
            System.out.println("Distance: " + distance(robot_x, robot_y, ball_x, ball_y));
            this.moveToObjectNode.execute(GameInfo.getBall());
            // try{
            //     Thread.sleep(100);
            // }catch(Exception e){}
        }
        
        // Calculate the direction from the ally to the ball
        double dy = (double) GameInfo.getBall().getY() - this.ally.getY();
        double dx = (double) GameInfo.getBall().getX() - this.ally.getX();
        
        // Set a fixed final orientation for demonstration purposes
        float final_orientation = (float) (Math.atan2(dy,dx));
        
        // Log the calculated dx, dy, and final orientation
        System.out.println("dx: " + dx);
        System.out.println("dy: " + dy);
        System.out.println("final: " + final_orientation);
        
        // Retrieve the current orientation of the ally
        float orientation = GameInfo.getAlly(this.TEST_ALLY_ID).getOrientation();
        // Normalize orientation to be within [0, 2*PI]
        if (orientation < 0) {
            orientation += 2 * Math.PI;
        }
        
        // Calculate the initial difference in orientation
        float diff = final_orientation - orientation;
        
        // Adjust orientation until the difference is within a threshold
        while (Math.abs(diff) > 0.1) {
            // Execute rotation command with proportional control (kP is assumed to be defined elsewhere)
            this.RotateBot.execute(diff * kP);
            
            // Update the orientation and difference for the next iteration
            orientation = GameInfo.getAlly(this.TEST_ALLY_ID).getOrientation();
            if (orientation < 0) {
                orientation += 2 * Math.PI;
            }
            diff = final_orientation - orientation;
            
            // Log the current orientation and difference
            System.out.println("orientation: " + orientation);
            System.out.println("diff: " + diff);
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