package main.java.core.util;

import proto.simulation.SslGcCommon;

import  main.java.core.constants.Team;

import static main.java.core.constants.ProgramConstants.objectConfig;
import static proto.simulation.SslSimulationControl.TeleportBall;
import static proto.simulation.SslSimulationControl.TeleportRobot;
import static proto.triton.FilteredObject.Ball;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionBall;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionRobot;

public class ProtobufUtils {
    public static TeleportRobot createTeleportRobot(Team team, int id, float x, float y, float orientation) {
        TeleportRobot.Builder teleportRobot = TeleportRobot.newBuilder();
        SslGcCommon.RobotId.Builder robotId = SslGcCommon.RobotId.newBuilder();
        if (team == Team.YELLOW)
            robotId.setTeam(SslGcCommon.Team.YELLOW);
        else
            robotId.setTeam(SslGcCommon.Team.BLUE);
        robotId.setId(id);
        teleportRobot.setId(robotId);
        teleportRobot.setX(objectConfig.cameraToObjectFactor * x);
        teleportRobot.setY(objectConfig.cameraToObjectFactor * y);
        teleportRobot.setOrientation(orientation);
        teleportRobot.setPresent(true);
        teleportRobot.setByForce(false);
        return teleportRobot.build();
    }

    public static TeleportBall createTeleportBall(float x, float y, float z) {
        return createTeleportBall(x, y, z, 0, 0, 0);
    }

    public static TeleportBall createTeleportBall(float x, float y, float z, float vx, float vy, float vz) {
        TeleportBall.Builder teleportBall = TeleportBall.newBuilder();
        teleportBall.setX(objectConfig.cameraToObjectFactor * x);
        teleportBall.setY(objectConfig.cameraToObjectFactor * y);
        teleportBall.setZ(objectConfig.cameraToObjectFactor * z);
        teleportBall.setVx(objectConfig.cameraToObjectFactor * vx);
        teleportBall.setVy(objectConfig.cameraToObjectFactor * vy);
        teleportBall.setVz(objectConfig.cameraToObjectFactor * vz);
        teleportBall.setByForce(false);
        return teleportBall.build();
    }

    public static Vector2d getPos(Robot robot) {
        return new Vector2d(robot.getX(), robot.getY());
    }

    public static Vector2d getPos(Ball ball) {
        return new Vector2d(ball.getX(), ball.getY());
    }

    public static Vector2d getPos(SSL_DetectionRobot robot) {
        return new Vector2d(robot.getX(), robot.getY());
    }

    public static Vector2d getPos(SSL_DetectionBall ball) {
        return new Vector2d(ball.getX(), ball.getY());
    }

    public static Vector2d getVel(Robot robot) {
        return new Vector2d(robot.getVx(), robot.getVy());
    }

    public static Vector2d getVel(Ball ball) {
        return new Vector2d(ball.getVx(), ball.getVy());
    }

    public static Vector2d getAcc(Robot robot) {
        return new Vector2d(robot.getAccX(), robot.getAccX());
    }

    public static Vector2d getAcc(Ball ball) {
        return new Vector2d(ball.getAccX(), ball.getAccX());
    }

    public static Vector2d getDribbleStartPos(Robot robot) {
        return new Vector2d(robot.getDribbleStartX(), robot.getDribbleStartY());
    }

}
