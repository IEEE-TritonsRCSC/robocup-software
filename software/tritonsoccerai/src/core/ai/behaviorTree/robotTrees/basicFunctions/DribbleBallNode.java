package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

import core.search.implementation.PathfindGridGroup;

import core.util.Vector2d;

import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import proto.simulation.SslSimulationRobotControl;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    private final Ally ally;

    // TODO Pathfinding algorithms needed
    public DribbleBallNode(Ally ally) {
        super("Dribble Ball Node: " + ally.toString(), ally);
        this.ally = ally;
    }

     /**
      * Check whether the robot is facing the ball or not.
      * Find the path to the ball and set dribbler speed.
      * Move into the ball at a speed that keeps the ball close to robot.
      */
    @Override
    public NodeState execute() {


        // TODO find path to the ball
        findPath(null);


        return NodeState.SUCCESS;
    }

    /**
     * Check whether the robot is facing to the ball or not.
     * Finds path to the target.
     */
    //TODO Have to define the PathToTarget class
    public PathToTarget findPath(Vector2d position) {
        // TODO
        return null;
    }

    /*
     * Publish the robot command to execute dribble 
     */
    public void publish_dribble(){
        // Set dribbler speed and publish the command
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(ally.getId());
        robotCommand.setDribblerSpeed(1);
        robotCommand.setKickSpeed(0);

        publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());
    }

}
