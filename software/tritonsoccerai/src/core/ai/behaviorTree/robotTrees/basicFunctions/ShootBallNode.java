package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;

public class ShootBallNode extends SequenceNode {

    public ShootBallNode() {
        this.sequence = new BTNode[2];
        this.sequence[0] = new TaskNode("Orient Robot") {
            @Override
            public NodeState execute() {
                // TODO
                return null;
            }
        };
        this.sequence[1] = new TaskNode("Kick Ball") {
            @Override
            public NodeState execute() {
                return null;
            }
        };
    }

}
