package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.util.Vector2d;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;

import main.java.core.constants.ProgramConstants;
import main.java.core.ai.GameInfo;

import proto.simulation.SslSimulationRobotControl;

import main.java.core.util.ObjectHelper;

import static main.java.core.constants.ProgramConstants.aiConfig;

import static proto.simulation.SslGcCommon.RobotId;
import static proto.triton.FilteredObject.Robot;

/**
 * Sends message to robot to kick ball with provided speed in provided direction
 * The robot should change its orientation if it is not facing the provided direction
 */
public class KickBallNode extends TaskNode {

    public KickBallNode(int allyID) {
        super("Kick Ball Node: " + allyID, allyID);
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
        if (ObjectHelper.hasOrientation(GameInfo.getAlly(allyID), direction, aiConfig.kickToPointAngleTolerance)) {
            SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

            robotCommand.setId(allyID);

            SslSimulationRobotControl.RobotMoveCommand.Builder moveCommand = SslSimulationRobotControl.RobotMoveCommand.newBuilder();
            SslSimulationRobotControl.MoveLocalVelocity.Builder localCommand = SslSimulationRobotControl.MoveLocalVelocity.newBuilder();
            localCommand.setForward(0.1f);
            localCommand.setLeft(0);
            localCommand.setAngular(0);
            moveCommand.setLocalVelocity(localCommand);
            robotCommand.setMoveCommand(moveCommand);
            robotCommand.setKickSpeed((float) (ProgramConstants.objectConfig.cameraToObjectFactor * velocity));
            robotCommand.setKickAngle(0);
            robotCommand.setDribblerSpeed(0);

            ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        }
        // TODO if robot is not facing the right direction, rotate 
        else{
            SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();

            robotCommand.setId(allyID);

            SslSimulationRobotControl.RobotMoveCommand.Builder moveCommand = SslSimulationRobotControl.RobotMoveCommand.newBuilder();
            SslSimulationRobotControl.MoveLocalVelocity.Builder localCommand = SslSimulationRobotControl.MoveLocalVelocity.newBuilder();
            localCommand.setForward(0);
            localCommand.setLeft(0);
            localCommand.setAngular(0);
            moveCommand.setLocalVelocity(localCommand);
            robotCommand.setMoveCommand(moveCommand);
            robotCommand.setKickSpeed((float) (ProgramConstants.objectConfig.cameraToObjectFactor * velocity));
            robotCommand.setKickAngle(0);
            robotCommand.setDribblerSpeed(0);

            ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand.build());

        }

        return null;
    }

}
