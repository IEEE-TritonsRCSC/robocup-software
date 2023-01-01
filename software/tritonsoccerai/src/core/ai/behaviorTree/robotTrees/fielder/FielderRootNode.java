package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;
import core.fieldObjects.robot.Ally;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Root node of fielder tree
 * If game is in open play, plays offense or defense
 * If referee command, takes appropriate action
 * Runs FielderRootRunnable at defined frequency
 */
public class FielderRootNode extends CompositeNode {

    private final FielderRootRunnable fielderRootRunnable;
    private final ScheduledThreadPoolExecutor executor;

    public FielderRootNode(Ally ally, ScheduledThreadPoolExecutor executor) {
        super("Fielder Root");

        this.fielderRootRunnable = new FielderRootRunnable(ally, executor);
        this.executor = executor;
    }

    /**
     * At a desired frequency, run FielderRootRunnable
     */
    @Override
    public NodeState execute() {
        // TODO
        executor.scheduleAtFixedRate(this.fielderRootRunnable, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
