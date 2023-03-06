package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.constants.ProgramConstants;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GKDefenseRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final GKDefenseRootService gkDefenseRootService;
    private Future gkDefenseRootFuture;

    public GKDefenseRootNode(ScheduledThreadPoolExecutor executor) {
        super();
        this.executor = executor;
        this.gkDefenseRootService = new GKDefenseRootService();
    }

    @Override
    public NodeState execute() {
        this.gkDefenseRootFuture = this.executor.scheduleAtFixedRate(this.gkDefenseRootService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

    public void stopExecution() {
        this.gkDefenseRootFuture.cancel(true);
    }

}
