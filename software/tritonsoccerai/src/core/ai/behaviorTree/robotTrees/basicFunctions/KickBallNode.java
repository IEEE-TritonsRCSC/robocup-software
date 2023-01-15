package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;
import static core.constants.ProgramConstants.objectConfig;
import proto.simulation.SslSimulationRobotControl;
import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import core.util.ObjectHelper;
import static core.constants.ProgramConstants.aiConfig;

import static proto.simulation.SslGcCommon.RobotId;


/**
 * Sends message to robot to kick ball with provided speed in provided direction
 */
public class KickBallNode extends TaskNode {

    public KickBallNode(Ally ally) {
        // TODO
        super("Kick Ball Node: " + ally.toString(), ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    /**
     * Sends message to robot to kick ball with provided speed in provided direction with/without chip
     */
    public NodeState execute(Vector2d direction, double velocity, boolean chip) {
        //if robot is facing the right direction, kick ball quickly
        if (ObjectHelper.hasOrientation(ally, direction, aiConfig.kickToPointAngleTolerance)) {
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

        robotCommand.setId(ally.getId());

        SslSimulationRobotControl.RobotMoveCommand.Builder moveCommand = SslSimulationRobotControl.RobotMoveCommand.newBuilder();
        SslSimulationRobotControl.MoveLocalVelocity.Builder localCommand = SslSimulationRobotControl.MoveLocalVelocity.newBuilder();
        localCommand.setForward(0.1f);
        localCommand.setLeft(0);
        localCommand.setAngular(0);
        moveCommand.setLocalVelocity(localCommand);
        robotCommand.setMoveCommand(moveCommand);
        robotCommand.setKickSpeed(objectConfig.cameraToObjectFactor * velocity);
        robotCommand.setKickAngle(0);
        robotCommand.setDribblerSpeed(0);

        // TODO Not sure how to publish robotcommand

        }else{
            // TODO if robot is not facing the right direction, rotate 
        }

        return null;
    }

}
