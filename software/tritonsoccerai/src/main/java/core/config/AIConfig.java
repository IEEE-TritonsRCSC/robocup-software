package main.java.core.config;

import static main.java.core.constants.ProgramConstants.aiConfig;
import static main.java.core.constants.ProgramConstants.objectConfig;

public class AIConfig {

    public int goalkeeperId;

    public float nodeRadius;
    public float boundSafety;
    public float robotSafety;
    public float penaltyScale;
    public float gridExtend;
    public float collisionSpeedScale;
    public float collisionExtrapolation;

    public float pathToFormationOccupyDist;

    public float kickToPointAngleTolerance;

    public float kickFromPosDistTolerance;

    public float goalShootKickFromSearchDist;
    public float goalShootKickFromSearchSpacing;
    public float goalShootKickToSearchSpacing;
    public float goalShootDistToObstaclesScoreFactor;
    public float goalShootDistToShooterScoreFactor;
    public float goalShootKickSpeed;

    public float passKickReceiverDistThreshold;
    public float passKickFromSearchDist;
    public float passKickFromSearchSpacing;
    public float passDistToObstaclesScoreFactor;
    public float passDistToPasserScoreFactor;
    public float passCatchBallSpeedThreshold;
    public float passCatchBallAngleTolerance;
    public float passKickSpeed;

    public float kpPos;
    public float kiPos;
    public float kdPos;
    public float kpOrientation;
    public float kiOrientation;
    public float kdOrientation;

    public float getNodeSpacing() {
        return 2 * nodeRadius;
    }

    public float calculateBoundPenalty(float dist) {
        return calculateGeneralPenalty(-dist, getBoundCollisionDist());
    }

    public float calculateGeneralPenalty(float dist, float collisionDistance) {
        return aiConfig.penaltyScale * (1 - (dist / collisionDistance));
    }

    public float getBoundCollisionDist() {
//        return objectConfig.objectToCameraFactor * objectConfig.robotRadius
//                + nodeRadius
//                + boundSafety;
        return nodeRadius + boundSafety;
    }

    public float calculateRobotPenalty(float dist) {
        return calculateGeneralPenalty(dist, getRobotCollisionDist());
    }

    public float getRobotCollisionDist() {
        return 2 * objectConfig.objectToCameraFactor * objectConfig.robotRadius
                + nodeRadius
                + robotSafety;
    }

    public float calculateRobotPenalty(float dist, float collisionExtension) {
        return calculateGeneralPenalty(dist, getRobotCollisionDist() + collisionExtension);
    }

}
