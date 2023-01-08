package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.ball.Ball;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootNode extends CompositeNode {

    private final TaskNode blockBall;

    public GKDefenseRootNode(Ball ball) {
        super("GK Defense Node");
        this.blockBall = new BlockBallNode(ball);
    }

    @Override
    public NodeState execute() {
        return this.blockBall.execute();
    }

}
