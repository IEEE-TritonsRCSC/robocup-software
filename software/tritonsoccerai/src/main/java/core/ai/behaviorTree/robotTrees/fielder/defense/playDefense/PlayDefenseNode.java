package main.java.core.ai.behaviorTree.robotTrees.fielder.defense.playDefense;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.constants.ProgramConstants;
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

    public PlayDefenseNode(int allyID, ScheduledThreadPoolExecutor executor) {
        super("Play Defense Node: " + allyID);
        this.executor = executor;
        this.playDefenseService = new PlayDefenseService(allyID);
    }

    /**
     * At defined frequency, takes the appropriate defensive action
     */
    @Override
    public NodeState execute() {
        System.out.println("Running playDefense service");
        this.playDefenseFuture = this.executor.scheduleAtFixedRate(this.playDefenseService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.SUCCESS;
    }

    public void stopExecution() {
        this.playDefenseFuture.cancel(true);
    }

}
