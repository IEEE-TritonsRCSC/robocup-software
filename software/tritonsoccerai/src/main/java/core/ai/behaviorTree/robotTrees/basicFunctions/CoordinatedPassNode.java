package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import main.java.core.constants.RobotConstants;
import static main.java.core.constants.ProgramConstants.aiConfig;
import static main.java.core.util.ObjectHelper.distToPath;
import static main.java.core.util.ProtobufUtils.getPos;
import main.java.core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static proto.triton.FilteredObject.Robot;

 /**
 * Defines the sequence of tasks needed to successfully perform a coordinated pass between two allies.
 * It is assumed that the passer kicks the ball from the spot when he has it.
 */
public class CoordinatedPassNode extends SequenceNode {

    private final int passerID;
    private final KickBallNode kickBall;

    public CoordinatedPassNode(int passerID) {
        super("Coordinated Pass Node: " + passerID);
        this.passerID = passerID;
        this.kickBall = new KickBallNode(passerID);
    }

    /**
     * 1. Identifies the best pass receiver.
     * 2. Sends message to the central coordinator with pass details.
     * 3. Performs pass
     */
    @Override
    public NodeState execute() {
        return null;
    }

    public NodeState execute(boolean chip) {
        // TODO Send message to central coordinator with pass details

        //kicks the ball to the pass receiver
        this.kickBall.execute(findPassShot(), RobotConstants.MAX_KICK_VELOCITY, false);
        return NodeState.SUCCESS;
    }

    /**
     * Identifies the best pass receiver
     */
    private Vector2d findPassShot() {
        // Might need to edit later to work with Proto Robots
        ArrayList<Robot> foesList = new ArrayList<>(GameInfo.getFoeFielders());
        ArrayList<Robot> alliesList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the passer
        alliesList.remove(GameInfo.getAlly(passerID));

        //add allys to the target list
        List<Vector2d> kickTos = new ArrayList<>();
        for(int i=0;i<alliesList.size();i++) {
			kickTos.add(getPos(alliesList.get(i)));
		}

        //add foes to the obstacles list
        obstacles.addAll(foesList);

        //best kick direction
        Vector2d bestKickTo = null;

        float maxScore = -Float.MAX_VALUE;

        // Choose the best pass receiver from allysList
        for (Vector2d kickTo : kickTos) {
            float distToObstacles = distToPath(getPos(GameInfo.getAlly(passerID)), kickTo, obstacles);

            // TODO Maybe have to change how to calculate the score
            float score = aiConfig.passDistToObstaclesScoreFactor * distToObstacles;

            if (score > maxScore) {
                bestKickTo = kickTo;
                maxScore = score;
            }
        }

    return bestKickTo;
    }


}
