package core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot;

import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootRunnable implements Runnable {

    private final TaskNode blockBall;

    public GKDefenseRootRunnable() {
        this.blockBall = new BlockBallNode();
    }

    /**
     * Always block ball
     */
    @Override
    public void run() {
        this.blockBall.execute();
    }

}
