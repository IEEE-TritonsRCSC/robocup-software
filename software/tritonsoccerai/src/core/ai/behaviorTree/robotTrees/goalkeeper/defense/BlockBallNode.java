package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.ball.Ball;
import core.util.Vector2d;

/**
 * Moves goalkeeper to optimal position to block ball
 */
public class BlockBallNode extends TaskNode {

    private Vector2d centerArc;
    private int radius;
    private Ball ball;

    public BlockBallNode(Ball ball) {
        super("Block ball node");

        this.centerArc = new Vector2d(0, 0); // arbitrary center arc
        this.radius = 2;
        this.ball = ball;
    }

    @Override
    public NodeState execute() {
        // make it so execute moves robot to the location
        return NodeState.SUCCESS;
    }

    public Vector2d findPositioningLocation() {
        // finds optimal vector on the arc
        Vector2d optimalLocation = new Vector2d(
            (this.ball.getX() - this.centerArc.x) * radius, 
            (this.ball.getY() - this.centerArc.y) * radius);
        return optimalLocation; 
    }

}
