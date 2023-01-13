package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.FieldObject;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Moves ally towards a particular field object, taking into account where it is moving towards
 */
public class MoveToObjectNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public MoveToObjectNode(Ally ally) {
        super("Move To Object Node: " + ally.toString(), ally);
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
    public NodeState execute(FieldObject object) {
        float TIME_CONSTANT = 0.5f;
        Vector2d position = object.getPos().add(object.getVel().scale(TIME_CONSTANT));
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

}
