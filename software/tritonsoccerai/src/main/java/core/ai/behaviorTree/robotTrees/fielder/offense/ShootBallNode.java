package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.KickBallNode;
import main.java.core.constants.RobotConstants;
import main.java.core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static main.java.core.constants.ProgramConstants.*;

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
    private final int allyID;

    public ShootBallNode(int allyID) {
        super("Shoot Ball Node: " + allyID);
        this.kickBall = new KickBallNode(allyID);
        this.allyID = allyID;
    }

    /**
     * Kicks ball as fast as possible in the optimal direction
     */
    public NodeState execute() {
        // kicks the ball
        execute(new Vector2d(0, GameInfo.getField().getFieldLength() / 2.0f));
        return NodeState.SUCCESS;
    }

    /**
     * Kicks ball as fast as possible in the optimal direction
     */
    public NodeState execute(Vector2d shotTo) {
        // kicks the ball
        NodeState result = this.kickBall.execute(shotTo.sub(getPos(GameInfo.getAlly(allyID))), RobotConstants.MAX_KICK_VELOCITY, false);
        if (result == NodeState.SUCCESS) {System.out.println("Shot ball.");}
        return NodeState.SUCCESS;
    }

    /**
     * Finds the direction of the optimal shot, 
     * returns null if robot is too far from goal 
     * or shot path is blocked
     */
    public Vector2d findShot() {

        // Get the field parameter
        SSL_GeometryFieldSize field = GameInfo.getField();

        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the ally closest to ball
        alliesList.remove(GameInfo.getAlly(allyID));

        //add the other allys and foes to the obstacles list
        obstacles.addAll(alliesList);
        obstacles.addAll(foesList);

        //get the goal parameters
        float goalMinX = -field.getGoalWidth() / 2f + 300f;
        float goalMaxX = field.getGoalWidth() / 2f - 300f;
        float goalY = field.getFieldLength() / 2f;
        float ballWidth = objectConfig.ballRadius * objectConfig.objectToCameraFactor;

        if (getPos(GameInfo.getAlly(allyID)).dist(new Vector2d(0, goalY)) > 4000) {
            return null;
        }

        List<Vector2d> kickTos = new ArrayList<>();
        kickTos.add(new Vector2d(goalMinX + (2 * ballWidth), goalY));
        kickTos.add(new Vector2d(goalMaxX - (2 * ballWidth), goalY));
        // for (float goalX = goalMinX; goalX < goalMaxX; goalX += aiConfig.goalShootKickToSearchSpacing)
        //     kickTos.add(new Vector2d(goalX, goalY));

        //best kick direction
        Vector2d bestKickTo = null;

        float maxScore = -1;

        //defines the best kick direction based on the position of the obstacles
        for (Vector2d kickTo : kickTos) {
            float distToObstacles = distToPath(getPos(GameInfo.getAlly(allyID)), kickTo, obstacles);

            // TODO Maybe have to change how to calculate the score
            float score = aiConfig.goalShootDistToObstaclesScoreFactor * distToObstacles;

            if (score > maxScore) {
                bestKickTo = kickTo;
                maxScore = score;
            }
        }

        // System.out.println(maxScore);

        if (maxScore < 100) {return null;}
        else {return bestKickTo;}
    }

}
