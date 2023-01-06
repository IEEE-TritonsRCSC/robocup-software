package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.FieldObject;
import core.fieldObjects.robot.Ally;

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
     * Calculates a position to move towards based on location
     * and movement of object
     */
    public NodeState execute(FieldObject object) {
        // TODO
        // calculate position to move to and pass this location as a parameter below
        this.moveToPositionNode.execute();
        return NodeState.SUCCESS;
    }

}
