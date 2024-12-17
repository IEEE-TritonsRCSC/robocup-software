package core.ai.behaviorTree.robotTrees.central;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.NodeState;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import core.constants.ProgramConstants;

public class CentralCoordinatorRoot extends CompositeNode {

    private final CentralCoordinatorService centralService;
    private final ScheduledThreadPoolExecutor executor;

    public CentralCoordinatorRoot(ScheduledThreadPoolExecutor executor){
        super("Central Coordinator Root");
        this.centralService = new CentralCoordinatorService();
        this.executor = executor;
    }

    /**
     * At a desired frequency, run CentralCoordinatorService
     */
    @Override
    public NodeState execute() {
        this.executor.scheduleAtFixedRate(this.centralService, ProgramConstants.INITIAL_DELAY,
                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.RUNNING;
    }

}
