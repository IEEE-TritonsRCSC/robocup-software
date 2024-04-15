package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.gkRoot;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import main.java.core.constants.ProgramConstants;
import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot.GKDefenseRootService;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot.GKOffenseRootService;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.*;

import static proto.gc.SslGcRefereeMessage.Referee;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * root node of goalkeeper tree
 * if game is in open play, takes offensive or defensive action
 * if referee command, takes appropriate action
 * checks for change in game status at defined frequency
 * if game status different, kills current branch execution
 * and starts execution of correct branch
 */
public class GoalkeeperRootService extends ServiceNode {

    private final ScheduledThreadPoolExecutor executor;

    private final ConditionalNode haveBall;
    private final GKOffenseRootService offense;
    private final GKDefenseRootService defense;

    private final GKHaltNode haltNode;
    private final GKStopNode stopNode;
    private final GKDirectFreeNode prepareDirectFreeNode;
    private final GKIndirectFreeNode prepareIndirectFreeNode;
    private final GKKickoffNode prepareKickoffNode;
    private final GKPenaltyNode preparePenaltyNode;
    private final GKNormalStartNode normalStartNode;
    private final GKBallPlacementNode ballPlacementNode;

    private Referee.Command commandCurrentlyRunning;
    private boolean onOffense;

    private Future<?> branchFuture;
    private BTNode currentlyExecutingNode;

    public GoalkeeperRootService(ScheduledThreadPoolExecutor executor) {
        super("GK Root Service");

        this.executor = executor;

        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new GKOffenseRootService();
        this.defense = new GKDefenseRootService();

        this.haltNode = new GKHaltNode();
        this.stopNode = new GKStopNode(this.haltNode);
        this.prepareDirectFreeNode = new GKDirectFreeNode();
        this.prepareIndirectFreeNode = new GKIndirectFreeNode();
        this.prepareKickoffNode = new GKKickoffNode();
        this.preparePenaltyNode = new GKPenaltyNode();
        this.normalStartNode = new GKNormalStartNode();
        this.ballPlacementNode = new GKBallPlacementNode();

        this.onOffense = false;
    }

    /**
     * Kill execution of currently-executing branch
     * Execute the correct branch
     */
    private void switchBranch() {
        if (this.currentlyExecutingNode != null) {
            // kill current thread
            this.currentlyExecutingNode.stopExecution();
            this.branchFuture.cancel(true);
        }
        // start new thread with executeCorrectBranch()
        if (this.commandCurrentlyRunning != GameInfo.getCurrCommand()) {
            executeCorrectBranch();
            this.commandCurrentlyRunning = GameInfo.getCurrCommand();
        }
        else {
            runOpenPlay();
        }
    }

    /**
     * Choose the correct branch to execute based on game state
     * and execute it in a new thread
     */
    private void executeCorrectBranch() {
        switch (GameInfo.getCurrCommand()) {
            case HALT:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.haltNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.haltNode;
                break;
            case STOP:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.stopNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.stopNode;
                break;
            case DIRECT_FREE_YELLOW, DIRECT_FREE_BLUE:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.prepareDirectFreeNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.prepareDirectFreeNode;
                break;
            case INDIRECT_FREE_YELLOW, INDIRECT_FREE_BLUE:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.prepareIndirectFreeNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.prepareIndirectFreeNode;
                break;
            case PREPARE_KICKOFF_YELLOW, PREPARE_KICKOFF_BLUE:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.prepareKickoffNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.prepareKickoffNode;
                break;
            case PREPARE_PENALTY_YELLOW, PREPARE_PENALTY_BLUE:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.preparePenaltyNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.preparePenaltyNode;
                break;
            case NORMAL_START:
                this.branchFuture = this.executor.submit(this.normalStartNode);
                this.currentlyExecutingNode = this.normalStartNode;
                break;
            case BALL_PLACEMENT_YELLOW, BALL_PLACEMENT_BLUE:
                this.branchFuture = this.executor.submit(this.ballPlacementNode);
                this.currentlyExecutingNode = this.ballPlacementNode;
                break;
            case FORCE_START:
                runOpenPlay();
                break;
            case TIMEOUT_YELLOW, TIMEOUT_BLUE:
                this.branchFuture = this.executor.scheduleAtFixedRate(this.haltNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
                this.currentlyExecutingNode = this.haltNode;
                break;
        }
    }

    /**
     * Check if game state and ball possession has changed
     * If so, switch branch
     */
    @Override
    public NodeState execute() {
        if (this.commandCurrentlyRunning != GameInfo.getCurrCommand()) {
            switchBranch();
        }
        else if (GameInfo.inOpenPlay()) {
            if (NodeState.isSuccess(this.haveBall.execute()) != this.onOffense) {
                this.onOffense = !this.onOffense;
                switchBranch();
            }
        }
        return NodeState.SUCCESS;
    }

    /**
     * Run correct open play node concurrently
     */
    private void runOpenPlay() {
        if (onOffense) {
            this.branchFuture = this.executor.scheduleAtFixedRate(this.offense, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
            this.currentlyExecutingNode = this.offense;
        }
        else {
            this.branchFuture = this.executor.scheduleAtFixedRate(this.defense, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
            this.currentlyExecutingNode = this.defense;
        }
    }

}
