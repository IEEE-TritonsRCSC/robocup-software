package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
//import core.fieldObjects.FieldObject;
//import proto.filtered_object.Robot;
import core.util.Vector2d;

import proto.filtered_object.*;

/**
 * Moves ally towards a particular field object, taking into account where it is moving towards
 */
public class MoveToObjectNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public MoveToObjectNode(Robot ally) {
        super("Move To Object Node: " + ally, ally);
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        return null;
    }

    /**
     * Calculates a position to move towards based on position
     * and velocity of object
     */
    public NodeState execute(FilteredObject object) {
        float TIME_CONSTANT = 0.5f;
        Vector2d position = getPos(object).add(object.getVel().scale(TIME_CONSTANT));
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

}
