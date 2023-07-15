package main.java.core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.constants.ProgramConstants;
import static proto.triton.FilteredObject.Robot;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Defines behavior for playing offense as a fielder
 * If ally has possession of ball, shoots or makes play
 * If ally doesn't have possession of ball, positions ally optimally
 */
public class OffenseRootNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final OffenseRootService offenseRootService;
    private Future offenseRootFuture;

    public OffenseRootNode(int allyID, ScheduledThreadPoolExecutor executor) {
        super("Offense Root Node: " + allyID);
        this.offenseRootService = new OffenseRootService(allyID);
        this.executor = executor;
    }

    /**
     * If ally has possession of ball, shoots or makes play
     * If ally doesn't have possession of ball, positions ally optimally
     */
    @Override
    public NodeState execute() {
        this.offenseRootFuture = this.executor.scheduleAtFixedRate(this.offenseRootService, ProgramConstants.INITIAL_DELAY,
                                                                     ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.SUCCESS;
    }

    public void stopExecution() {
        this.offenseRootFuture.cancel(true);
    }

}
