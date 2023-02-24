package java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.HaltNode;
import static proto.triton.FilteredObject.Robot;

/**
 * Handles Stop game state
 * Slows down robot to under 1.5 m/s while keeping at least
 * 0.5 m distance from ball
 */
public class StopNode extends SequenceNode {

    private final Robot ally;
    private final HaltNode haltNode;

    public StopNode(Robot ally, HaltNode haltNode) {
        super("Stop Node: " + ally);
        this.ally = ally;
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
