package core.ai.behaviorTree.robotTrees.fielder.defense.playDefense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;
import static proto.triton.FilteredObject.Robot;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * At defined frequency, either chases ball or cuts a passing lane
 */
public class PlayDefenseNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final PlayDefenseService playDefenseService;
    private Future playDefenseFuture;

    public PlayDefenseNode(Robot ally, ScheduledThreadPoolExecutor executor) {
        super("Play Defense Node: " + ally);
        this.executor = executor;
        this.playDefenseService = new PlayDefenseService(ally);
    }

    /**
     * At defined frequency, takes the appropriate defensive action
     */
    @Override
    public NodeState execute() {
        this.playDefenseFuture = this.executor.scheduleAtFixedRate(this.playDefenseService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.SUCCESS;
    }

    public void stopExecution() {
        this.playDefenseFuture.cancel(true);
    }

}
