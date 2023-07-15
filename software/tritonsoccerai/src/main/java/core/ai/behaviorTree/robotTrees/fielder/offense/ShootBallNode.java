package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.KickBallNode;
import main.java.core.constants.RobotConstants;
import main.java.core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static main.java.core.constants.ProgramConstants.aiConfig;

import static main.java.core.util.ObjectHelper.distToPath;
import static main.java.core.util.ProtobufUtils.getPos;
import static proto.triton.FilteredObject.FilteredWrapperPacket;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Shoots ball in the calculated optimal direction at max velocity.
 * It is assumed that the ally kicks the ball from the spot when he has it.
 */
public class ShootBallNode extends SequenceNode {

    private final KickBallNode kickBall;

    public ShootBallNode(int allyID) {
        super("Shoot Ball Node: " + allyID);
        this.kickBall = new KickBallNode(allyID);
    }

    /**
     * Kicks ball as fast as possible in the optimal direction
     */
    public NodeState execute() {
        // kicks the ball
        this.kickBall.execute(findShot(), RobotConstants.MAX_KICK_VELOCITY, false);
        return NodeState.SUCCESS;
    }

    /**
     * Finds the direction of the optimal shot
     */
    private Vector2d findShot() {

        // Get the field parameter
        SSL_GeometryFieldSize field = GameInfo.getField();

        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the ally closest to ball
        alliesList.remove(GameInfo.getAllyClosestToBall());

        //add the other allys and foes to the obstacles list
        obstacles.addAll(alliesList);
        obstacles.addAll(foesList);

        //get the goal parameters
        float goalMinX = -field.getGoalWidth() / 2f + 300f;
        float goalMaxX = field.getGoalWidth() / 2f - 300f;
        float goalY = field.getFieldLength() / 2f;

        
        List<Vector2d> kickTos = new ArrayList<>();
        for (float goalX = goalMinX; goalX < goalMaxX; goalX += aiConfig.goalShootKickToSearchSpacing)
            kickTos.add(new Vector2d(goalX, goalY));

        //best kick direction
        Vector2d bestKickTo = null;

        float maxScore = -Float.MAX_VALUE;

        //defines the best kick direction based on the position of the obstacles
        for (Vector2d kickTo : kickTos) {
            float distToObstacles = distToPath(getPos(GameInfo.getAllyClosestToBall()), kickTo, obstacles);

            // TODO Maybe have to change how to calculate the score
            float score = aiConfig.goalShootDistToObstaclesScoreFactor * distToObstacles;

            if (score > maxScore) {
                bestKickTo = kickTo;
                maxScore = score;
            }
        }

        return bestKickTo;
    }

}
