package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;
import core.fieldObjects.robot.Ally;

public class HaltNode extends BTNode {
    public HaltNode(Ally ally) {

    }

    // TODO

    @Override
    public NodeState execute() {
        return NodeState.SUCCESS;
    }

}
