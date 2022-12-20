package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines sequence of tasks to be performed to dribble ball
 */
public class DribbleBallNode extends SequenceNode {

    private final Ally ally;

    public DribbleBallNode(Ally ally) {
        // TODO
        super("Dribble Ball Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public NodeState execute() {
        // TODO
        return null;
    }

}
