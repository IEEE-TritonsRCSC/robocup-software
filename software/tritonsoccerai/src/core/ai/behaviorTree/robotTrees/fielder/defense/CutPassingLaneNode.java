package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import proto.filtered_object.Robot;
import core.util.ObjectHelper;

/**
 * Cuts passing lane between ball and a foe
 */
public class CutPassingLaneNode extends TaskNode {

    private final MoveToObjectNode moveToObjectNode;

    public CutPassingLaneNode(Robot ally) {
        super("Cut Passing Lane Node: " + ally, ally);
        this.moveToObjectNode = new MoveToObjectNode(ally);
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    @Override
    public NodeState execute() {
        moveTowardFoe(ObjectHelper.identifyFoeToGuard(ally, GameInfo.getFoeFielders()));
        return NodeState.SUCCESS;
    }

    /**
     * Moves ally toward a given foe
     */
    private void moveTowardFoe(Robot foe) {
        this.moveToObjectNode.execute(foe);
    }

}
