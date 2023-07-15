package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.ObjectHelper;
import main.java.core.util.Vector2d;

import static main.java.core.util.ProtobufUtils.getPos;

import java.util.ArrayList;

/**
 * Handles Prepare Indirect Free game state
 */
public class IndirectFreeNode extends TaskNode {

    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final MoveToObjectNode moveToObjectNode;
    private final PositionSelfNode positionSelfNode;

    public IndirectFreeNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Prepare Indirect Free Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToObjectNode = new MoveToObjectNode(allyID);
        this.positionSelfNode = new PositionSelfNode(allyID);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall()) {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
                DISTANCE_CONSTANT = 1;
                while (getPos(GameInfo.getAlly(allyID)).dist(desiredLocation) > DISTANCE_CONSTANT) {
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
                this.moveToObjectNode.execute(ObjectHelper.identifyFoeToGuard(GameInfo.getAlly(allyID), opponents));
            }
        }
        return NodeState.SUCCESS;
    }

}
