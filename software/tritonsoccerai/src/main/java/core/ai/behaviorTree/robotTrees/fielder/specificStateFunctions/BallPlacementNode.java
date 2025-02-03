package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import static proto.triton.FilteredObject.Robot;

import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.awardedBall;
import core.util.Vector2d;

import core.constants.ProgramConstants;
import static core.constants.RobotConstants.*;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;


/**
 * Handles Ball Placement game state
 * If our possession, place ball at designated location
 */
public class BallPlacementNode extends TaskNode {
    
    private final ClosestToBallNode closestToBallNode;
    private final DribbleBallNode dribbleBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final ChaseBallNode chaseBallNode;
    private boolean positioned;

    public BallPlacementNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.dribbleBallNode = new DribbleBallNode(allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setAvoidBall(true);
        this.chaseBallNode = new ChaseBallNode(allyID);
        this.positioned = false;
    }

    /**
     * If our possession AND robot closest to ball, dribble ball to placement location
     * Otherwise, move away from placement location
     */
    @Override
    public NodeState execute() {
        Vector2d finalPos = GameInfo.getBallPlacementLocation();

        // TO DO : Need to add condition for it to stop dribbling. 

        if (awardedBall() && (closestToBallNode.execute() == NodeState.SUCCESS)) {
            float DISTANCE_CONSTANT = 1000;
            if (!GameInfo.getPossessBall(allyID)) {
                Vector2d ballPos = new Vector2d(GameInfo.getBall().getX(),GameInfo.getBall().getY());
                Vector2d robotPos = getPos(GameInfo.getAlly(allyID));

                Vector2d moveDirection = new Vector2d(
                    finalPos.x - ballPos.x,
                    finalPos.y - ballPos.y
                );
                
                Vector2d moveDirectionUnit = new Vector2d(
                    moveDirection.x / moveDirection.mag(),
                    moveDirection.y / moveDirection.mag()
                );
                
                Vector2d initRobotPos = new Vector2d(
                    ballPos.x - moveDirectionUnit.x * DISTANCE_CONSTANT,
                    ballPos.y - moveDirectionUnit.y * DISTANCE_CONSTANT
                );

                float distanceToInit = robotPos.dist(initRobotPos);
                
                if (distanceToInit <= 100) {
                    this.positioned = true;
                }

                if (!this.positioned) {
                    this.moveToPositionNode.execute(initRobotPos);
                }
                else {
                    this.moveToPositionNode.setAvoidBall(false);
                    this.chaseBallNode.execute();
                    this.dribbleBallNode.execute(finalPos);
                }
                
            }
            else {
                this.chaseBallNode.execute();
                this.dribbleBallNode.execute(finalPos);
                try {
                    TimeUnit.MILLISECONDS.sleep(ProgramConstants.LOOP_DELAY);
                } catch (Exception e) {}
            }
        }
        else {
            // move away from placement location if close to it
            Robot ally = GameInfo.getAlly(allyID);
            Vector2d allyPos = getPos(ally);
            // System.out.println("ballposition:" + ballPos);
            // System.out.println("allyposition:" + allyPos);
            Vector2d ballToAlly = getPos(ally).sub(finalPos);
            // System.out.println("Dist:" + ballToAlly);
            if (ballToAlly.mag() < 2000) {
                Vector2d targetPos =  finalPos.add(new Vector2d(10 * (allyPos.x - finalPos.x), 10 * (allyPos.y - finalPos.y)));
                // System.out.println("targetPos: " + targetPos);
                // MoveToPositionNode MoveToPosition = new MoveToPositionNode(allyID);
                // MoveToPosition.execute(targetPos);
                this.moveToPositionNode.execute(targetPos);
            }
           
        }
        return NodeState.SUCCESS;
    }

}
