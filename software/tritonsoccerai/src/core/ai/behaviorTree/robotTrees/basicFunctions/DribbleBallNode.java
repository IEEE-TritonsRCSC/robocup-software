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
        
        findPath(null);

        return null;
    }

    /**
     * Check whether the robot is facing to the ball or not.
     * Finds path to the target.
     */
    //TODO Have to define the PathToTarget class
    public void findPath(Vector2d ballPosition) {
        // TODO Check whether the robot is facing to the ball or not.

        // find path to the ball
        Vector2d allyPos = super.ally.getPos();

        // Pathfinding to ballPosition
        LinkedList<Node2d> route = pathfindGridGroup.findRoute(ally.getId(), allyPos, ballPosition);
        Vector2d next = pathfindGridGroup.findNext(ally.getId(), route);
        
        publish_dribble();
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
