package core.ai.behaviorTree.robotTrees.fielder.defense.playDefense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.constants.ProgramConstants;
import core.fieldObjects.robot.Ally;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * At defined frequency, either chases ball or cuts a passing lane
 */
public class PlayDefenseNode extends CompositeNode {

    private final ScheduledThreadPoolExecutor executor;
    private final PlayDefenseService playDefenseService;

    public PlayDefenseNode(Ally ally, ScheduledThreadPoolExecutor executor) {
        super("Play Defense Node: " + ally.toString());
        this.executor = executor;
        this.playDefenseService = new PlayDefenseService(ally);
    }

    /**
     * At defined frequency, takes the appropriate defensive action
     */
    @Override
    public NodeState execute() {
        this.executor.scheduleAtFixedRate(this.playDefenseService, ProgramConstants.INITIAL_DELAY,
                                        ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
        return NodeState.SUCCESS;
    }

}
