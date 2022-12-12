package core.ai.behaviorTree.robotTrees.specificStateFunctions;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;

public class HaltNode extends BTNode {

    // TODO

    @Override
    public NodeState execute() {
        return NodeState.SUCCESS;
    }

}
