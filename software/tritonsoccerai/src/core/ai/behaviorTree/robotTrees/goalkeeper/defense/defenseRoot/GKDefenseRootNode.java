package core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GKDefenseRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final GKDefenseRootService gkDefenseRootService;

    public GKDefenseRootNode(ScheduledThreadPoolExecutor executor) {
        super();
        this.executor = executor;
        this.gkDefenseRootService = new GKDefenseRootService();
    }

    @Override
    public NodeState execute() {
        this.executor.scheduleAtFixedRate(this.gkDefenseRootService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
