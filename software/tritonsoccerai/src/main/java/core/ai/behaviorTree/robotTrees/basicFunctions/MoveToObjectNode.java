package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import static proto.triton.FilteredObject.*;
import main.java.core.util.Vector2d;
import main.java.core.ai.GameInfo;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ProtobufUtils.getVel;

import static main.java.core.constants.ProgramConstants.objectConfig;

/**
 * Moves ally towards a particular field object, taking into account where it is moving towards
 */
public class MoveToObjectNode extends TaskNode {

    private final MoveToPositionNode moveToPositionNode;

    public MoveToObjectNode(int allyID) {
        super("Move To Object Node: " + allyID, allyID);
        this.moveToPositionNode = new MoveToPositionNode(allyID);
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
        // System.out.println(position);
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

    /**
     * Calculates a position to move towards based on position
     * and velocity of robot
     */
    public NodeState execute(Robot robot) {
        float TIME_CONSTANT = 0.5f;
        Vector2d direction = new Vector2d(0.0f, -1.0f * GameInfo.getField().getFieldLength());
        direction = direction.sub(getPos(robot));
        Vector2d position = getPos(robot).add(getVel(robot).scale(TIME_CONSTANT)).add(direction.norm().scale(1.5 * objectConfig.robotRadius));
        this.moveToPositionNode.execute(position);
        return NodeState.SUCCESS;
    }

    /**
     * Sets the dribble setting
     * @param dribbleOn whether robot velocity should be restricted to max dribbling speed
     */
    public void setDribbleOn(boolean dribbleOn) {
        this.moveToPositionNode.setDribbleOn(dribbleOn);
    }

}
