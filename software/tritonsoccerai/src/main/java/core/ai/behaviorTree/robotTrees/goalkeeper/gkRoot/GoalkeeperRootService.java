package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.gkRoot;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot.GKDefenseRootNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot.GKOffenseRootNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.*;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
    private final GKOffenseRootNode offense;
    private final GKDefenseRootNode defense;

    private final GKHaltNode haltNode;
    private final GKStopNode stopNode;
    private final GKDirectFreeNode prepareDirectFreeNode;
    private final GKIndirectFreeNode prepareIndirectFreeNode;
    private final GKKickoffNode prepareKickoffNode;
    private final GKPenaltyNode preparePenaltyNode;
    private final GKNormalStartNode normalStartNode;
    private final GKBallPlacementNode ballPlacementNode;

    private GameState stateCurrentlyRunning;
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
        this.offense = new GKOffenseRootNode(executor);
        this.defense = new GKDefenseRootNode(executor);

        this.haltNode = new GKHaltNode();
        this.stopNode = new GKStopNode(this.haltNode);
        this.prepareDirectFreeNode = new GKDirectFreeNode();
        this.prepareIndirectFreeNode = new GKIndirectFreeNode();
        this.prepareKickoffNode = new GKKickoffNode();
        this.preparePenaltyNode = new GKPenaltyNode();
        this.normalStartNode = new GKNormalStartNode();
        this.ballPlacementNode = new GKBallPlacementNode();

        this.stateCurrentlyRunning = GameState.OPEN_PLAY;
        this.onOffense = false;
        runOpenPlay();
    }

    /**
     * Kill execution of currently-executing branch
     * Execute the correct branch
     */
    private void switchBranch() {
        // stop current branch execution
        this.currentlyExecutingNode.stopExecution();
        this.branchFuture.cancel(true);
        // submit new branch with executeCorrectBranch()
        executeCorrectBranch();
        this.stateCurrentlyRunning = GameInfo.getCurrState();
    }

    /**
     * Choose the correct branch to execute based on game state
     * and execute it in a new thread
     */
    private void executeCorrectBranch() {
        switch (GameInfo.getCurrState()) {
            case HALT:
                this.branchFuture = this.executor.submit(this.haltNode);
                this.currentlyExecutingNode = this.haltNode;
            case STOP:
                this.branchFuture = this.executor.submit(this.stopNode);
                this.currentlyExecutingNode = this.stopNode;
            case PREPARE_DIRECT_FREE:
                this.branchFuture = this.executor.submit(this.prepareDirectFreeNode);
                this.currentlyExecutingNode = this.prepareDirectFreeNode;
            case PREPARE_INDIRECT_FREE:
                this.branchFuture = this.executor.submit(this.prepareIndirectFreeNode);
                this.currentlyExecutingNode = this.prepareIndirectFreeNode;
            case PREPARE_KICKOFF:
                this.branchFuture = this.executor.submit(this.prepareKickoffNode);
                this.currentlyExecutingNode = this.prepareKickoffNode;
            case PREPARE_PENALTY:
                this.branchFuture = this.executor.submit(this.preparePenaltyNode);
                this.currentlyExecutingNode = this.preparePenaltyNode;
            case NORMAL_START:
                this.branchFuture = this.executor.submit(this.normalStartNode);
                this.currentlyExecutingNode = this.normalStartNode;
            case BALL_PLACEMENT:
                this.branchFuture = this.executor.submit(this.ballPlacementNode);
                this.currentlyExecutingNode = this.ballPlacementNode;
            case FORCE_START, OPEN_PLAY:
                runOpenPlay();
        }
    }

    /**
     * Check if game state and ball possession has changed
     * If so, switch branch
     */
    @Override
    public NodeState execute() {
        if (this.stateCurrentlyRunning != GameInfo.getCurrState()) {
            switchBranch();
        }
        else if (stateCurrentlyRunning == GameState.OPEN_PLAY) {
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
            this.branchFuture = this.executor.submit(this.offense);
            this.currentlyExecutingNode = this.offense;
        }
        else {
            this.branchFuture = this.executor.submit(this.defense);
            this.currentlyExecutingNode = this.defense;
        }
    }

}
