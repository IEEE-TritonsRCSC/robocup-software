package core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;
import core.fieldObjects.robot.Ally;

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

    public OffenseRootNode(Ally ally, ScheduledThreadPoolExecutor executor) {
        super("Offense Root");
        this.offenseRootService = new OffenseRootService(ally);
        this.executor = executor;
    }

    /**
     * If ally has possession of ball, shoots or makes play
     * If ally doesn't have possession of ball, positions ally optimally
     */
    @Override
    public NodeState execute() {
        this.executor.scheduleAtFixedRate(this.offenseRootService, ProgramConstants.INITIAL_DELAY, ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.SUCCESS;
    }

}
