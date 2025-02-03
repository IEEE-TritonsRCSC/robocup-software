package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;

import static proto.triton.FilteredObject.Robot;

import static core.util.ProtobufUtils.getPos;
import static core.util.ProtobufUtils.getVel;

import proto.simulation.SslSimulationRobotControl;
import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

import static core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static core.constants.RobotConstants.DRIBBLE_RPM;

import static core.constants.ProgramConstants.objectConfig;
import core.constants.ProgramConstants;
import static proto.triton.FilteredObject.Ball;
import static proto.triton.FilteredObject.Robot;
import core.ai.GameInfo;
import core.util.Vector2d;
import static proto.triton.CoordinatedPassInfo.CoordinatedPass;
import core.messaging.Exchange;

/**
 * Defines task of catching ball in motion
 */
public class CatchBallNode extends TaskNode {

    private Robot robot;
    private Ball ball;
    private MoveToPositionNode moveToPosition;
    private final float kP = 3.0f;
    private final float DIST_THRESHOLD = objectConfig.robotRadius + objectConfig.ballRadius * 4;
    private final float ANGLE_THRESHOLD = (float) (15.0 * Math.PI / 180.0);
    private float prevDistDiff = Float.MAX_VALUE;

    public CatchBallNode(int allyID) {
        super("Catch Ball Node: " + allyID, allyID);
        moveToPosition = new MoveToPositionNode(allyID);
    }

    @Override
    public NodeState execute() {
        robot = GameInfo.getAlly(this.allyID);
        ball = GameInfo.getBall();

        Vector2d ballToRobot = getPos(robot).sub(getPos(ball));
        Vector2d proj = getVel(ball).project(ballToRobot);

        Vector2d closestPointToBallPath = getPos(ball).add(proj);

        this.moveToPosition.execute(closestPointToBallPath);

        // Turn on dibbler and begin rotating in the direction of the ball's velocity
        SslSimulationRobotControl.RobotCommand.Builder robotCommand = SslSimulationRobotControl.RobotCommand.newBuilder();
        robotCommand.setId(allyID);
        robotCommand.setDribblerSpeed(DRIBBLE_RPM);
        robotCommand.setKickSpeed(0);

        float angleDiff = Vector2d.angleDifference(robot.getOrientation(), getVel(ball).angle() + (float) Math.PI); // adding pi to flip direction
        
        RobotMoveCommand.Builder moveCommand = RobotMoveCommand.newBuilder();
        MoveLocalVelocity.Builder localVel = MoveLocalVelocity.newBuilder();
        localVel.setAngular(angleDiff * kP);
        moveCommand.setLocalVelocity(localVel);
        robotCommand.setMoveCommand(moveCommand);
        
        ProgramConstants.commandPublishingModule.publish(AI_BIASED_ROBOT_COMMAND, robotCommand);

        // If pass was successfully recieved or deemed no longer able to recieve, send message to end pass
        boolean success = this.passRecieved();
        boolean failure = this.passFailed();
        if (success || failure) {
            CoordinatedPass.Builder passInfo = CoordinatedPass.newBuilder();
            passInfo.setReceiverID(-1);
            ProgramConstants.commandPublishingModule.publish(Exchange.CENTRAL_COORDINATOR_PASSING, passInfo.build());
            
            if (success) return NodeState.SUCCESS;
            else return NodeState.FAILURE;
        }

        return NodeState.RUNNING;
    }

    // Check if ball is likely in the dribbler
    public boolean passRecieved() {
        Vector2d robotToBall = getPos(ball).sub(getPos(robot));
        float ballDist = robotToBall.mag();
        float angleDiff = Vector2d.angleDifference(robot.getOrientation(), robotToBall.angle()); 
        return (robotToBall.mag() < DIST_THRESHOLD) && (Math.abs(angleDiff) < ANGLE_THRESHOLD);
    }

    // Check if distance to the ball is increasing
    public boolean passFailed() {
        Vector2d robotToBall = getPos(ball).sub(getPos(robot));
        float currDistDiff = robotToBall.mag();
        if (currDistDiff > prevDistDiff) {
            return true;
        }
        prevDistDiff = currDistDiff;
        return false;
    }

}
