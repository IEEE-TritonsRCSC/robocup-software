package core.ai.behaviorTree.robotTrees.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;

public class NormalStartNode extends SequenceNode {

    // TODO

    @Override
    public NodeState execute() {
        return NodeState.SUCCESS;
    }

}
