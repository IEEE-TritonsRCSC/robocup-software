package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.KickBallNode;
import core.constants.RobotConstants;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

import core.fieldObjects.robot.Robot;
import core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static core.constants.ProgramConstants.aiConfig;

//import protofiles
import static core.util.ObjectHelper.distToPath;
import static proto.triton.FilteredObject.FilteredWrapperPacket;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Shoots ball in the calculated optimal direction at max velocity.
 * It is assumed that the ally kicks the ball from the spot when he has it.
 */
public class ShootBallNode extends SequenceNode {

    private final KickBallNode kickBall;

    public ShootBallNode(Ally ally) {
        super("Shoot Ball Node: " + ally.toString());
        this.kickBall = new KickBallNode(ally);
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

        // TODO Get the field parameter
        SSL_GeometryFieldSize field = wrapper.getField();

        ArrayList<Foe> foesList = new ArrayList<>(GameInfo.getFoes());
        ArrayList<Ally> allysList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the ally closest to ball
        allysList.remove(GameInfo.getAllyClosestToBall());

        //add the other allys and foes to the obstacles list
        obstacles.addAll(allysList);
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
            float distToObstacles = distToPath(GameInfo.getAllyClosestToBall().getPos(), kickTo, obstacles);

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
