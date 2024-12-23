package main.java.core.module.testModule;


import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.constants.ProgramConstants;
import main.java.core.module.testModule.TestModule;
import main.java.core.util.Vector2d;
import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;


public class CoordinatePassTestModule extends TestModule{
    private final int TEST_ALLY_ID;
    private CoordinatedPassNode coordinatedPassNode;
    private MoveToObjectNode moveToObjectNode;
    private Future<?> moveFuture;
    private Future<?> coordinatedPassFuture;


    public CoordinatePassTestModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
        // this.TEST_ALLY_ID = GameInfo.getAllyClosestToBall().getId();
        this.TEST_ALLY_ID = 0;
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
        this.moveFuture.cancel(true);
        this.coordinatedPassFuture.cancel(true);
    }


    @Override
    public void run() {
        super.run();
        // System.out.println(GameInfo.getField());
        // float robot_x = GameInfo.getAllyClosestToBall().getX(), robot_y = GameInfo.getAllyClosestToBall().getY();
        // float ball_x = GameInfo.getBall().getX(), ball_y = GameInfo.getBall().getY();


        this.moveToObjectNode = new MoveToObjectNode(TEST_ALLY_ID);
        this.moveToObjectNode.setDribbleOn(true);
        this.coordinatedPassNode = new CoordinatedPassNode(TEST_ALLY_ID);
        

        while(!GameInfo.getPossessBall(TEST_ALLY_ID)) {
            // robot_x = GameInfo.getAllyClosestToBall().getX();
            // robot_y = GameInfo.getAllyClosestToBall().getY();
            // ball_x = GameInfo.getBall().getX();
            // ball_y = GameInfo.getBall().getY();
            // System.out.println("Distance: " + distance(robot_x, robot_y, ball_x, ball_y));
            this.moveToObjectNode.execute(GameInfo.getBall());
            // try{
            //     Thread.sleep(100);
            // }catch(Exception e){}
        }


        while (true){
            // System.out.println("Coordinated Pass testing started");
            try{
                this.coordinatedPassNode.execute();
                Thread.sleep(50);
            }catch(Exception e){
                
                e.printStackTrace();
                break;
                // System.out.println(e.printStackTrace());
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
    }


    private float distance(float x1, float y1, float x2, float y2){
        return (float) Math.pow((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2), 0.5);
    }
}



