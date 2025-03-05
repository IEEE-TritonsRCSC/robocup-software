package core.constants;

/**
 * Contains robot constants including constraints
 */
public class RobotConstants {

    public static final int GOALKEEPER_ID = 0;
    public static final double MAX_KICK_VELOCITY = 3200.0; // change this
    public static final float DRIBBLE_RPM = 1200.0f;
    public static final double ROBOT_WIDTH = 2.0; // change this

    public static final float MAX_DRIBBLE_MOVE_VELOCITY = 50.0f;
    public static final float MAX_MOVE_VELOCITY = 70.0f;
    public static final float MAX_DRIBBLE_ROTATE_ANGULAR = 2.0f;

    public static final float DRIBBLE_THRESHOLD = 100.0f;

    public static final float MOVE_VELOCITY_DAMPENER = 0.0011f;

    public static final float MAX_ANGULAR_VELOCITY = 5.0f; // Adjust based on robot's rotation speed
    public static final float ANGULAR_DECELERATION_RADIUS = 1000.0f; // Distance threshold for deceleration

}


