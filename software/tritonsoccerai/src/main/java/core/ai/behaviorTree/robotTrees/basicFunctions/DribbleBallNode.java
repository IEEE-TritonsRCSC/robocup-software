package core.ai.behaviorTree.robotTrees.basicFunctions;

import java.util.*;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.constants.ProgramConstants;
import static core.constants.ProgramConstants.aiConfig;
import core.util.Vector2d;
import core.search.implementation.PathfindGridGroup;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.GameInfo;

import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static core.constants.RobotConstants.DRIBBLE_RPM;

import proto.simulation.SslSimulationRobotControl;
import static proto.triton.FilteredObject.Robot;

import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.distToPath;

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
        robotCommand.setKickSpeed(0.0f);

        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        return NodeState.SUCCESS;
    }

    public NodeState execute(Vector2d location) {
        this.execute();
        this.moveToPositionNode.execute(location);
        
        return NodeState.SUCCESS;
    }

    public static Vector2d findDribblingDirection(int allyID) {
        float directionMagnitude = 10f; // magnitude of the direction vector that is returned

        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoeFielders());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        ArrayList<Robot> obstacles = new ArrayList<>();

        //remove the dribbler
        Robot robot = GameInfo.getAlly(allyID);
        alliesList.remove(robot);

        //add all foes and allies to obstacles
        obstacles.addAll(foesList);
        obstacles.addAll(alliesList);

        List<Vector2d> directions = new ArrayList<>();
        //add all directions with obstacles into list of directions
        for(int i = 0; i < obstacles.size(); i++) {
            if(distToPath(getPos(GameInfo.getAlly(allyID)), getPos(obstacles.get(i)), obstacles) <= 10) {
                directions.add(getPos(obstacles.get(i)));
            }
        }

        //if there are directions in which there are no obstacles
        for (double j = 0; j <= Math.PI/2; j += Math.PI/32) {
            boolean leftSideValid = true;
            boolean rightSideValid = true;

            for (Robot obstacle : obstacles) {
                // Check both left half and right half; scanning from the front outwards in a semicircle
                if (leftSideValid) {
                    if (!(Math.atan2(obstacle.getX() - robot.getX(), obstacle.getY() - robot.getY()) + j < 0.001)) {
                        leftSideValid = false; // obstacle on the left at angle -j
                    }
                }
                if (rightSideValid) {
                    if (Math.atan2(obstacle.getX() - robot.getX(), obstacle.getY() - robot.getY()) - j < 0.001) {
                        rightSideValid = false; // obstacle on the right at angle j
                    }
                }
            }

            if (leftSideValid) {
                // Return correct vector on the left half of the semicircle
                if (j == 0) return new Vector2d(0f, directionMagnitude);
                if (j == Math.PI/2) return new Vector2d((float) -directionMagnitude, 0f);
                else return new Vector2d((float) -directionMagnitude * (float) Math.tan(j), (float) directionMagnitude);
            }
            if (rightSideValid) {
                // Return correct vector on the right half of the semicircle
                if (j == 0) return new Vector2d(0f, (float) directionMagnitude);
                if (j == Math.PI/2) return new Vector2d((float) directionMagnitude, 0f);
                else return new Vector2d((float) directionMagnitude * (float) Math.tan(j), (float) directionMagnitude);
            }
        }

        Vector2d bestDirection = null;
        float maxScore = -Float.MAX_VALUE;

        //find direction with furthest away obstacle that prioritises going forwards
        for(Vector2d direction : directions) {
            float distToObstacles = distToPath(getPos(GameInfo.getAlly(allyID)), direction, obstacles);
            //determine best direction using distance to obstacle and the y-coordinate
            float score = aiConfig.passDistToObstaclesScoreFactor * distToObstacles * direction.y;
            if (score > maxScore) {
                bestDirection = direction;
                maxScore = score;
            }
        }
        return bestDirection;
    }

}