package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

import core.search.implementation.PathfindGridGroup;

import core.util.Vector2d;

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
      * Move into ball at a speed that keeps the ball close to robot.
      * Find the path to the ball and set dribbler speed.
      */
    @Override
    public NodeState execute() {
        // TODO find path to the ball

        // Set dribbler speed
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(ally.getId());
        robotCommand.setDribblerSpeed(1);
        robotCommand.setKickSpeed(0);

        // TODO Not sure how to publish robotcommand

        return null;
    }

    /**
     * Dribbles ball toward a specific location
     */
    public NodeState execute(Vector2d position) {
        // TODO
        return NodeState.SUCCESS;
    }

}
