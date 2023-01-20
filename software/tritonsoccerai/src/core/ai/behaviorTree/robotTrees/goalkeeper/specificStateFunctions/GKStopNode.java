package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.HaltNode;
import core.fieldObjects.robot.Ally;

public class GKStopNode extends SequenceNode {

    private final Ally ally;
    private final HaltNode haltNode;

    public GKStopNode(Ally ally, HaltNode haltNode) {
        super("GK Stop Node: " + ally.toString());
        this.ally = ally;
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
