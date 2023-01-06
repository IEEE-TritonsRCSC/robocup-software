package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Positions ally at optimal position
 */
public class PositionSelfNode extends TaskNode {

    public PositionSelfNode(Ally ally) {
        super("Position Self Node: " + ally.toString(), ally);
    }

    /**
     * Decides where to position self, then moves to that location
     */
    @Override
    public NodeState execute() {
        // TODO
        return null;
    }

    /**
     * Finds optimal location to position self
     */
    private Vector2d findPositioningLocation() {
        // TODO
        return null;
    }

}
