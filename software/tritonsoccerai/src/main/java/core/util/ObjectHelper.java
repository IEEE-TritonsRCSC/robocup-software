package main.java.core.util;

import main.java.core.ai.GameInfo;

import java.util.ArrayList;
import java.util.List;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ProtobufUtils.getVel;
import static main.java.core.util.ProtobufUtils.getAcc;
import static proto.triton.FilteredObject.Ball;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotMoveCommand;
import static proto.simulation.SslSimulationRobotControl.MoveLocalVelocity;

public class ObjectHelper {
    
    public static boolean hasOrientation(Robot robot, Vector2d facePos, float angleTolerance) {
        float targetOrientation = (float) Math.atan2(facePos.y - robot.getY(), facePos.x - robot.getX());
        return hasOrientation(robot, targetOrientation, angleTolerance);
    }

    public static boolean hasOrientation(Robot robot, float orientation, float angleTolerance) {
        float angleDifference = Vector2d.angleDifference(orientation, robot.getOrientation());
        return Math.abs(angleDifference) < angleTolerance;
    }

    public static boolean hasPos(Robot robot, Vector2d pos, float distanceTolerance) {
        Vector2d robotPos = new Vector2d(robot.getX(), robot.getY());
        return robotPos.dist(pos) < distanceTolerance;
    }

    public static boolean ballWillArriveAtTarget(Vector2d pos, float delta, float distanceTolerance) {
        return willArriveAtTarget(getPos(GameInfo.getBall()), getVel(GameInfo.getBall()), getAcc(GameInfo.getBall()), pos, delta, distanceTolerance);
    }

    public static boolean willArriveAtTarget(Vector2d pos, Vector2d vel, Vector2d acc, Vector2d target, float delta,
                                             float distanceTolerance) {
        Vector2d predictPos = predictPos(pos, vel, acc, delta);
        return predictPos.dist(target) < distanceTolerance;
    }

    public static Vector2d predictPos(Vector2d pos, Vector2d vel, Vector2d acc, float delta) {
        // TODO ADD ACC PREDICTION
        return pos.add(vel.scale(delta));
    }

    public static Vector2d predictPos(Ball ball, float delta) {
        return predictPos(getPos(ball), getVel(ball), getAcc(ball), delta);
    }

    public static boolean willArriveAtTarget(Robot robot, Vector2d target, float delta, float distanceTolerance) {
        return willArriveAtTarget(getPos(robot), getVel(robot), getAcc(robot), target, delta, distanceTolerance);
    }

    public static boolean ballIsMovingTowardTarget(Vector2d pos, float angleTolerance) {
        return isMovingTowardTarget(getPos(GameInfo.getBall()), getVel(GameInfo.getBall()), pos, angleTolerance);
    }

    public static boolean isMovingTowardTarget(Vector2d pos, Vector2d vel, Vector2d target, float angleTolerance) {
        Vector2d towardTarget = target.sub(pos);
        return vel.angle(towardTarget) < angleTolerance;
    }

    public static boolean isMovingTowardTarget(Vector2d target, float speedThreshold, float angleTolerance) {
        return isMovingTowardTarget(getPos(GameInfo.getBall()), getVel(GameInfo.getBall()), target, speedThreshold, angleTolerance);
    }

    public static boolean isMovingTowardTarget(Vector2d pos, Vector2d vel, Vector2d target,
                                               float speedThreshold, float angleTolerance) {
        Vector2d towardTarget = target.sub(pos);
        return vel.mag() > speedThreshold && vel.angle(towardTarget) < angleTolerance;
    }

    public static boolean isMovingTowardTarget(Robot robot, Vector2d pos, float angleTolerance) {
        return isMovingTowardTarget(getPos(robot), getVel(robot), pos, angleTolerance);
    }

    public static boolean isMovingTowardTarget(Robot robot, Vector2d pos, float speedThreshold, float angleTolerance) {
        return isMovingTowardTarget(getPos(robot), getVel(robot), pos, speedThreshold, angleTolerance);
    }

    public static Vector2d predictRobotPos(Robot robot, float delta) {
        return predictPos(getPos(robot), getVel(robot), getAcc(robot), delta);
    }

    public static Vector2d predictBallPos(float delta) {
        return predictPos(getPos(GameInfo.getBall()), getVel(GameInfo.getBall()), getAcc(GameInfo.getBall()), delta);
    }

    public static float predictOrientation(Robot robot, float delta) {
        return predictOrientation(robot.getOrientation(), robot.getAngular(), robot.getAccAngular(), delta);
    }

    public static float predictOrientation(float orientation, float angular, float accAngular, float delta) {
        // TODO ADD ACC PREDICTION
        return orientation
                + delta * angular;
    }

    public static Vector2d getAllyGoal(SSL_GeometryFieldSize field) {
        return new Vector2d(0, -field.getFieldLength() / 2f);
    }

    public static Vector2d getFoeGoal(SSL_GeometryFieldSize field) {
        return new Vector2d(0, field.getFieldLength() / 2f);
    }

    public static boolean ballIsInAllyGoal(SSL_GeometryFieldSize field) {
        float goalX = -field.getGoalWidth() / 2f;
        float goalY = -field.getFieldLength() / 2f - field.getGoalDepth();
        float goalWidth = field.getGoalWidth();
        float goalHeight = field.getGoalDepth();
        return getPos(GameInfo.getBall()).isInRect(goalX, goalY, goalWidth, goalHeight);
    }

    public static boolean ballIsInFoeGoal(SSL_GeometryFieldSize field) {
        float goalX = -field.getGoalWidth() / 2f;
        float goalY = field.getFieldLength() / 2f;
        float goalWidth = field.getGoalWidth();
        float goalHeight = field.getGoalDepth();
        return getPos(GameInfo.getBall()).isInRect(goalX, goalY, goalWidth, goalHeight);
    }

    public static boolean ballIsInBounds(SSL_GeometryFieldSize field) {
        return isInBounds(getPos(GameInfo.getBall()), field);
    }

    public static boolean isInBounds(Vector2d pos, SSL_GeometryFieldSize field) {
        return pos.isInRect(-field.getFieldWidth() / 2f,
                -field.getFieldLength() / 2f,
                field.getFieldWidth(),
                field.getFieldLength());
    }

    public static boolean isInBounds(Robot robot, SSL_GeometryFieldSize field) {
        return isInBounds(getPos(robot), field);
    }

    public static float distToPath(Vector2d from, Vector2d to, List<Robot> robots) {
        List<Vector2d> points = new ArrayList<>();
        robots.forEach(robot -> points.add(getPos(robot)));
        return Vector2d.distToPath(from, to, points);
    }

    public static boolean checkDistToPath(Vector2d from, Vector2d to, List<Robot> robots, float dist) {
        List<Vector2d> points = new ArrayList<>();
        robots.forEach(robot -> points.add(getPos(robot)));
        return Vector2d.checkDistToPath(from, to, points, dist);
    }

    public static boolean isWithinDist(Vector2d target, List<Robot> robots, float dist) {
        for (Robot robot : robots)
            if (target.dist(getPos(robot)) < dist)
                return true;
        return false;
    }

    public static float getMinDist(Vector2d target, List<Robot> robots) {
        float minDist = Float.MAX_VALUE;
        for (Robot robot : robots)
            minDist = Math.min(minDist, target.dist(getPos(robot)));
        return minDist;
    }

    /**
     * Gets closest robot to a given point on field
     * 
     * @param target Target location on field
     * @param robots List of robots to consider
     * @return c\Closest robot
     */
    public static Robot getNearestRobot(Vector2d target, List<Robot> robots) {
        Robot closestRobot = null;
        float minDist = Float.MAX_VALUE;
        for (Robot robot : robots) {
            float dist = target.dist(getPos(robot));
            if (dist < minDist) {
                closestRobot = robot;
                minDist = dist;
            }
        }
        return closestRobot;
    }

    /**
     * Returns set of robots within a distance threshold of a specific
     * point on field
     * 
     * @param target Target location on field
     * @param robots Set of robots to consider
     * @param distThreshold Distance threshold
     * @return List of robots within threshold
     */
    public static List<Robot> getNearRobots(Vector2d target, List<Robot> robots, float distThreshold) {
        List<Robot> nearRobots = new ArrayList<>();
        for (Robot robot : robots) {
            float dist = target.dist(getPos(robot));
            if (dist < distThreshold) {
                nearRobots.add(robot);
            }
        }
        return nearRobots;
    }

    /**
     * Identifies a foe for ally to guard based on positions of all allies and foes
     * Refer to figure 8 for detailed explanation
     * 
     * @param ally Ally for which to identify foe to guard
     * @param foes List of foes to consider as possible assignment
     * @return Foe for ally to guard
     */
    public static Robot identifyFoeToGuard(Robot ally, ArrayList<Robot> foes) {
        // make copy of GameInfo.getFoeFielders() and sort it
        ArrayList<Robot> foesInOrderOfDistance = new ArrayList<>(foes);
        foesInOrderOfDistance.remove(GameInfo.getFoeClosestToBall());
        foesInOrderOfDistance.sort((o1, o2) -> (int) (getPos(o1).dist(getPos(ally)) - getPos(o2).dist(getPos(ally))));
        // make copy of GameInfo.getFielders() and remove the ally closest to ball
        ArrayList<Robot> allyGuarders = new ArrayList<>(GameInfo.getFielders());
        allyGuarders.remove(GameInfo.getAllyClosestToBall());

        if ((foesInOrderOfDistance.size() == 0) || (allyGuarders.size() == 0)) {
            return null;
        }

        // For each foe, find n if ally is the nth farthest allyGuarder from foe
        // place sum will be n + foe's index in foesInOrderOfDistance
        int minPlaceSum = Integer.MAX_VALUE;
        int minPlaceSumIndex = 0;
        for (int i = 0; i < foesInOrderOfDistance.size(); i++) {
            Robot foe = foesInOrderOfDistance.get(i);
            float distToFoe = getPos(foe).dist(getPos(ally));
            int place = 0;
            for (Robot guarder : allyGuarders) {
                if (getPos(foe).dist(getPos(guarder)) <= distToFoe) {
                    place++;
                }
            }
            if ((i + place) < minPlaceSum) {
                minPlaceSum = i + place;
                minPlaceSumIndex = i;
            }
        }
        // return the foe with the lowest place sum
        return foesInOrderOfDistance.get(minPlaceSumIndex);
    }

    /**
     * Generates a move command from the local perspective given the attributes
     * of the move command from the global perspective
     * 
     * @param vx global velocity in x direction (m/s)
     * @param vy global velocity in the y direction (m/s)
     * @param angular global angular velocity (rad/s)
     * @param orientation orientation of robot expressed in radians
     * @param id robot id
     * @return Built RobotCommand with local move command
     */
    public static RobotCommand generateLocalMoveCommand(float vx, float vy, float angular, float orientation, int id) {
        // TODO: Fix this method 

        float angular_correction = 0.135f;
        float rotation = (float) (-(orientation + angular * angular_correction) + (Math.PI / 2));

        // set up local attribute of robot
        // what is those number represent (?)
        // what is the difference between global and local numbers(?)
        float local_vx = (float) (vx * Math.cos(rotation) - vy * Math.sin(rotation));
        float local_vy = (float) (vx * Math.sin(rotation) + vy * Math.cos(rotation));
        float local_angular = angular;

        System.out.println("Orientation is " + orientation);
        System.out.println("Local vx is " + local_vx);
        System.out.println("Local vy is " + local_vy);

        // actually move the robot / run potential motors
        // dont need to connected to the motor(?)
        RobotCommand.Builder robotCommand = RobotCommand.newBuilder();
        robotCommand.setId(id);
        RobotMoveCommand.Builder moveCommand = RobotMoveCommand.newBuilder();
        MoveLocalVelocity.Builder localVel = MoveLocalVelocity.newBuilder();
        localVel.setLeft(-local_vx);
        localVel.setForward(local_vy);
        localVel.setAngular(local_angular);
        moveCommand.setLocalVelocity(localVel);
        robotCommand.setMoveCommand(moveCommand);

        return robotCommand.build();
    }
    
}
