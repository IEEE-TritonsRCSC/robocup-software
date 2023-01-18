package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Stop game state
 * Slows down robot to under 1.5 m/s while keeping at least
 * 0.5 m distance from ball
 */
public class StopNode extends SequenceNode {

    private final Ally ally;
    private final HaltNode haltNode;

    public StopNode(Ally ally, HaltNode haltNode) {
        super("Stop Node: " + ally.toString());
        this.ally = ally;
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
