package core.ai.behaviorTree.robotTrees.fielder.defense;

import java.util.ArrayList;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import static proto.triton.FilteredObject.Robot;
import static core.util.ObjectHelper.identifyFoeToGuard;
import static core.util.ProtobufUtils.getPos;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.util.Vector2d;

/**
 * Cuts passing lane between ball and a foe
 */
public class CutPassingLaneNode extends TaskNode {

    private final MoveToObjectNode moveToObjectNode;
    private final MoveToPositionNode moveToPositionNode;
    private final ChaseBallNode chaseBallNode;

    public CutPassingLaneNode(int allyID) {
        super("Cut Passing Lane Node: " + allyID, allyID);
        this.moveToObjectNode = new MoveToObjectNode(allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.chaseBallNode = new ChaseBallNode(allyID);
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    @Override
    public NodeState execute() {
        execute(GameInfo.getFoeFielders());
        return NodeState.SUCCESS;
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    public NodeState execute(ArrayList<Robot> foes) {
        Robot foeToGuard = identifyFoeToGuard(GameInfo.getAlly(allyID), foes);
        if (foeToGuard != null) {
            moveTowardFoe(foeToGuard);
        }
        else {
            Vector2d direction = new Vector2d(-1 * getPos(GameInfo.getBall()).x, (-1 * GameInfo.getField().getFieldLength() / 2) - getPos(GameInfo.getBall()).y);
            direction = direction.norm().scale(GameInfo.getField().getFieldLength() / 6);
            this.moveToPositionNode.execute(getPos(GameInfo.getBall()).add(direction));
        }
        return NodeState.SUCCESS;
    }

    /**
     * Moves ally toward a given foe
     */
    private void moveTowardFoe(Robot foe) {
        this.moveToObjectNode.execute(foe);
    }

}
