package core.ai.behaviorTree.robotTrees.goalkeeper.gkRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GoalkeeperRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final GoalkeeperRootService gkRootService;

    public GoalkeeperRootNode(ScheduledThreadPoolExecutor executor) {
        super("Goalkeeper Root Node");
        this.executor = executor;
        this.gkRootService = new GoalkeeperRootService(executor);
    }

    /**
     * At a desired frequency, run gkRootService
     */
    @Override
    public NodeState execute() {
        executor.scheduleAtFixedRate(this.gkRootService, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
