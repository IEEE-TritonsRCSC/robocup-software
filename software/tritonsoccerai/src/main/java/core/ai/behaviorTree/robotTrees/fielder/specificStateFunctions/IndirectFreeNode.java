package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.CutPassingLaneNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import static proto.triton.FilteredObject.Robot;
import core.util.ObjectHelper;
import core.util.Vector2d;

import static core.constants.ProgramConstants.objectConfig;
import static core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import static core.util.ProtobufUtils.getPos;
import static core.util.ObjectHelper.awardedBall;

import java.util.ArrayList;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends TaskNode {

    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final PositionSelfNode positionSelfNode;
    private final CutPassingLaneNode cutPassingLaneNode;

    public IndirectFreeNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Prepare Indirect Free Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setAvoidBall(true);
        this.positionSelfNode = new PositionSelfNode(allyID);
        this.cutPassingLaneNode = new CutPassingLaneNode(allyID);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (awardedBall())  {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                if (!GameInfo.getPossessBall(allyID)) {
                    Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - DRIBBLE_THRESHOLD - objectConfig.robotRadius);
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            else {
                this.positionSelfNode.execute();
            }
        }
        else {
            ArrayList<Robot> opponents;
            DISTANCE_CONSTANT = 700;
            opponents = new ArrayList<Robot>(GameInfo.getFoeFielders());
            for (Robot foe : opponents) {
                if (getPos(foe).dist(getPos(GameInfo.getBall())) < DISTANCE_CONSTANT) {
                    opponents.remove(foe);
                }
            }
            this.cutPassingLaneNode.execute(opponents);
        }
        return NodeState.SUCCESS;
    }

}
