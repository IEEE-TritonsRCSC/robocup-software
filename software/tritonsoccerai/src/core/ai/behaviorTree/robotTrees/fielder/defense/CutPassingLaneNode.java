package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

public class CutPassingLaneNode extends TaskNode {

    private Ally ally;

    public CutPassingLaneNode(Ally ally) {
        super();
        this.ally = ally;
    }

    @Override
    public NodeState execute() {
        moveTowardFoe(identifyFoeToGuard());
        return NodeState.SUCCESS;
    }

    private Foe identifyFoeToGuard() {
        // TODO
        return null;
    }

    private void moveTowardFoe(Foe foe) {
        // TODO
    }

}
