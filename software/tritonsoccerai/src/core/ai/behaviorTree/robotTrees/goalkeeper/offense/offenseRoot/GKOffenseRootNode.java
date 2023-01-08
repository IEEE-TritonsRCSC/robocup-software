package core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GKOffenseRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final GKOffenseRootRunnable gkOffenseRootRunnable;

    public GKOffenseRootNode(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
        this.gkOffenseRootRunnable = new GKOffenseRootRunnable();
    }

    @Override
    public NodeState execute() {
        this.executor.scheduleAtFixedRate(this.gkOffenseRootRunnable, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
