package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.constants.RobotConstants;
import static core.constants.ProgramConstants.aiConfig;
import static core.util.ObjectHelper.distToPath;
import static core.util.ProtobufUtils.getPos;
import core.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static proto.triton.FilteredObject.Robot;

 /**
 * Defines the sequence of tasks needed to successfully perform a coordinated pass between two allies.
 * It is assumed that the passer kicks the ball from the spot when he has it.
 */
public class CoordinatedPassNode extends SequenceNode {

    private final Robot passer;
    private final KickBallNode kickBall;

    public CoordinatedPassNode(Robot passer) {
        super("Coordinated Pass Node: " + passer);
        this.passer = passer;
        this.kickBall = new KickBallNode(passer);
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
        ArrayList<Robot> allysList = new ArrayList<>(GameInfo.getFielders());
        List<Robot> obstacles = new ArrayList<>();

        //remove the passer
        allysList.remove(passer);

        //add allys to the target list
        List<Vector2d> kickTos = new ArrayList<>();
        for(int i=0;i<allysList.size();i++) {
			kickTos.add(getPos(allysList.get(i)));
		}

        //add foes to the obstacles list
        obstacles.addAll(foesList);

        //best kick direction
        Vector2d bestKickTo = null;

        float maxScore = -Float.MAX_VALUE;

        // Choose the best pass receiver from allysList
        for (Vector2d kickTo : kickTos) {
            float distToObstacles = distToPath(getPos(passer), kickTo, obstacles);

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
