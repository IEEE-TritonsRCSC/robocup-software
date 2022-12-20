package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

/**
 * Cuts passing lane between ball and a foe
 */
public class CutPassingLaneNode extends TaskNode {

    private final Ally ally;

    public CutPassingLaneNode(Ally ally) {
        super();
        this.ally = ally;
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    @Override
    public NodeState execute() {
        moveTowardFoe(identifyFoeToGuard());
        return NodeState.SUCCESS;
    }

    /**
     * Identifies a foe for ally to guard based on its position and which foes are unguarded
     */
    private Foe identifyFoeToGuard() {
        // TODO
        return null;
    }

    /**
     * Moves ally toward a given foe
     */
    private void moveTowardFoe(Foe foe) {
        // TODO
    }

}
