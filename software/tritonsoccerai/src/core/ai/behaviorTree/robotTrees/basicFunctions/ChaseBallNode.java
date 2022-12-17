package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

public class ChaseBallNode extends TaskNode {

    private Ally ally;

    public ChaseBallNode(Ally ally) {
        super();
        this.ally = ally;
    }

    @Override
    public NodeState execute() {
        // send command to move toward ball
        return NodeState.SUCCESS;
    }

}
