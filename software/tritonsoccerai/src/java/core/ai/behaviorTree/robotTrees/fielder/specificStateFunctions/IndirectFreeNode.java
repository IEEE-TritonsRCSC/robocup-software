package java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import static proto.triton.FilteredObject.Robot;
import java.core.util.ObjectHelper;
import java.core.util.Vector2d;

import static java.core.util.ProtobufUtils.getPos;

import java.util.ArrayList;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends TaskNode {

    private final Robot ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final MoveToObjectNode moveToObjectNode;
    private final PositionSelfNode positionSelfNode;

    public IndirectFreeNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Indirect Free Node: " + ally, ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(ally);
        this.moveToObjectNode = new MoveToObjectNode(ally);
        this.positionSelfNode = new PositionSelfNode(ally);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall()) {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
                DISTANCE_CONSTANT = 1;
                while (getPos(this.ally).dist(desiredLocation) > DISTANCE_CONSTANT) {
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            else {
                while (true) {
                    this.positionSelfNode.execute();
                }
            }
        }
        else {
            ArrayList<Robot> opponents;
            DISTANCE_CONSTANT = 7;
            while (true) {
                opponents = new ArrayList<Robot>(GameInfo.getFoeFielders());
                for (Robot foe : opponents) {
                    if (getPos(foe).dist(getPos(GameInfo.getBall())) < DISTANCE_CONSTANT) {
                        opponents.remove(foe);
                    }
                }
                this.moveToObjectNode.execute(ObjectHelper.identifyFoeToGuard(this.ally, opponents));
            }
        }
        return NodeState.SUCCESS;
    }

}
