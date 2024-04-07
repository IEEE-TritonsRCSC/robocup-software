package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.constants.ProgramConstants;
import main.java.core.util.Vector2d;
import main.java.core.search.implementation.PathfindGridGroup;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static main.java.core.constants.RobotConstants.DRIBBLE_RPM;

import proto.simulation.SslSimulationRobotControl;
import proto.triton.FilteredObject.Robot;

import static proto.triton.FilteredObject.Robot;

import java.util.ArrayList;
import java.util.List;

import core.ai.GameInfo;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    PathfindGridGroup pathfindGridGroup;
    private final MoveToPositionNode moveToPositionNode;

    public DribbleBallNode(int allyID) {
        super("Dribble Ball Node: " + allyID, allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setDribbleOn(true);
    }

     /**
      * Move the kicker at speed that keeps the ball close to the robot.
      * Publish the robot command to execute dribble. 
      */
    @Override
    public NodeState execute() {
        // Set dribbler speed and publish the command
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(allyID);
        robotCommand.setDribblerSpeed(DRIBBLE_RPM);
        robotCommand.setKickSpeed(0);

        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        return NodeState.SUCCESS;
    }

    public NodeState execute(Vector2d location) {
        this.execute();
        this.moveToPositionNode.execute(location);
        
        return NodeState.SUCCESS;
    }

    public Vector2d findDribblingDirection() {
        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoeFielders());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the dribbler
        alliesList.remove(GameInfo.getAlly(allyID));

        //add all foes and allies to obstacles
        obstacles.addAll(foesList);
        obstacles.addAll(alliesList);

        List<Vector2d> directions = new ArrayList<>();
        //add all directions with obstacles into list of directions
        for(int i = 0; i < obstacles.size(); i++) {
            if(distToPath(getPos(GameInfo.getAlly(allyID)), getPos(obstacles.get(i), obstacles)) <= 10) {
                directions.add(getPos(obstacles.get(i)));
            }
        }

        //if there are directions in which there are no obstacles
        for(int i = 0; i < obstacles.size(); i++) {
            for(double j = 0; j < Math.PI; j+= Math.PI/6) {
                //if there is a direction in which an obstacle exists, return direction vector
                if(!Math.abs(Math.atan2(obstacle.y - curr.y, obstacle.x, curr.x) - j) < 0.001) {
                    return new Vector2d(10, 10*tan(j));
                }
            }
        }

        Vector2d bestDirection = new Vector2d();
        float maxScore = -Float.MAX_VALUE;

        //find direction with furthest away obstacle that prioritises going forwards
        for(Vector2d direction: directions) {
            float distToObstacles = distToPath(getPos(GameInfo.getAlly(allyID)), direction, obstacles);
            //determine best direction using distance to obstacle and the y-coordinate
            float score = aiConfig.passDistToObstaclesScoreFactor * distToObstacles * direction.y;
            if (score > maxScore) {
                bestDirection = direction;
                maxScore = score;
            }
        }
        return maxScore;
    }

}