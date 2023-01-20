package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

import core.search.implementation.PathfindGridGroup;
import core.search.node2d.Node2d;

import core.util.Vector2d;
import java.util.LinkedList;
import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import proto.simulation.SslSimulationRobotControl;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    private final Ally ally;
    PathfindGridGroup pathfindGridGroup;

    public DribbleBallNode(Ally ally) {
        super("Dribble Ball Node: " + ally.toString(), ally);
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
