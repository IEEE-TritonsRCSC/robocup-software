package java.core.ai.behaviorTree.robotTrees.basicFunctions;

import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.search.implementation.PathfindGridGroup;
import static java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import proto.simulation.SslSimulationRobotControl;
import static proto.triton.FilteredObject.Robot;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    private final Robot ally;
    PathfindGridGroup pathfindGridGroup;

    public DribbleBallNode(Robot ally) {
        super("Dribble Ball Node: " + ally, ally);
        this.ally = ally;
    }

     /**
      * Move the kicker at speed that keeps the ball close to the robot.
      * Publish the robot command to execute dribble. 
      */
    @Override
    public NodeState execute() {
        // Set dribbler speed and publish the command
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(ally.getId());
        robotCommand.setDribblerSpeed(1);
        robotCommand.setKickSpeed(0);

        publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        return null;
    }


}
