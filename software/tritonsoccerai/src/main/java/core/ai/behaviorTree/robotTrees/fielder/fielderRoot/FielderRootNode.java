package main.java.core.ai.behaviorTree.robotTrees.fielder.fielderRoot;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.constants.ProgramConstants;
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

    public FielderRootNode(int allyID, ScheduledThreadPoolExecutor executor) {
        super("Fielder Root Node: " + allyID);

        this.fielderRootService = new FielderRootService(allyID, executor);
        this.executor = executor;
    }

    /**
     * At a desired frequency, run FielderRootService
     */
    @Override
    public NodeState execute() {
        executor.scheduleAtFixedRate(this.fielderRootService, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
