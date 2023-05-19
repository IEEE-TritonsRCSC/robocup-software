package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import static proto.triton.FilteredObject.*;
import main.java.core.util.Vector2d;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ProtobufUtils.getVel;

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
     * and velocity of ball
     */
    public NodeState execute(Ball ball) {
        float TIME_CONSTANT = 0.5f;
        Vector2d position = getPos(ball).add(getVel(ball).scale(TIME_CONSTANT));
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

    /**
     * Calculates a position to move towards based on position
     * and velocity of robot
     */
    public NodeState execute(Robot robot) {
        float TIME_CONSTANT = 0.5f;
        Vector2d position = getPos(robot).add(getVel(robot).scale(TIME_CONSTANT));
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

}
