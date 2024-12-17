package core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GKOffenseRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final GKOffenseRootService gkOffenseRootService;
    private Future gkOffenseRootFuture;

    public GKOffenseRootNode(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
        this.gkOffenseRootService = new GKOffenseRootService();
    }

    @Override
    public NodeState execute() {
        this.gkOffenseRootFuture = this.executor.scheduleAtFixedRate(this.gkOffenseRootService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

    public void stopExecution() {
        gkOffenseRootFuture.cancel(true);
    }

}
