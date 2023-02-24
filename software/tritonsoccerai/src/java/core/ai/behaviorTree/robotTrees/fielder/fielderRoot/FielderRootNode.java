package java.core.ai.behaviorTree.robotTrees.fielder.fielderRoot;

import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import java.core.constants.ProgramConstants;
import static proto.triton.FilteredObject.Robot;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Root node of fielder tree
 * If game is in open play, plays offense or defense
 * If referee command, takes appropriate action
 * Runs FielderRootService at defined frequency
 */
public class FielderRootNode extends CompositeNode {

    private final FielderRootService fielderRootService;
    private final ScheduledThreadPoolExecutor executor;

    public FielderRootNode(Robot ally, ScheduledThreadPoolExecutor executor) {
        super("Fielder Root");

        this.fielderRootService = new FielderRootService(ally, executor);
        this.executor = executor;
    }

    /**
     * At a desired frequency, run FielderRootService
     */
    @Override
    public NodeState execute() {
        // TODO
        executor.scheduleAtFixedRate(this.fielderRootService, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
