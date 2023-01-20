package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Handles Halt game state
 * Stops robot within 2 seconds without manipulating ball
 */
public class HaltNode extends SequenceNode {

    private final Ally ally;

    public HaltNode(Ally ally) {
        super("Halt Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public NodeState execute() {
        // TODO stop the robot's motion
        return NodeState.SUCCESS;
    }

}
