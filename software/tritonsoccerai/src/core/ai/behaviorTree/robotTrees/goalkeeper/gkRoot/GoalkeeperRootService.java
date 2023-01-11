package core.ai.behaviorTree.robotTrees.goalkeeper.gkRoot;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot.GKDefenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.offenseRoot.GKOffenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.*;

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

    private Thread branchThread;

    public GoalkeeperRootService(ScheduledThreadPoolExecutor executor) {
        super("GK Root Service");

        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new GKOffenseRootNode(executor);
        this.defense = new GKDefenseRootNode(executor);

        this.haltNode = new GKHaltNode();
        this.stopNode = new GKStopNode();
        this.prepareDirectFreeNode = new GKDirectFreeNode();
        this.prepareIndirectFreeNode = new GKIndirectFreeNode();
        this.prepareKickoffNode = new GKKickoffNode();
        this.preparePenaltyNode = new GKPenaltyNode();
        this.normalStartNode = new GKNormalStartNode();
        this.ballPlacementNode = new GKBallPlacementNode();

        this.stateCurrentlyRunning = GameInfo.getCurrState();
        this.onOffense = false;

        this.branchThread = new Thread();
        this.branchThread.setDaemon(true);
    }

    /**
     * Kill execution of currently-executing branch
     * Execute the correct branch
     */
    private void switchBranch() {
        // kill current thread
        this.branchThread.interrupt();
        // start new thread with executeCorrectBranch()
        executeCorrectBranch();
        this.stateCurrentlyRunning = GameInfo.getCurrState();
    }

    /**
     * Choose the correct branch to execute based on game state
     * and execute it in a new thread
     */
    private void executeCorrectBranch() {
        switch (GameInfo.getCurrState()) {
            case HALT -> this.branchThread = new Thread(this.haltNode);
            case STOP -> this.branchThread = new Thread(this.stopNode);
            case PREPARE_DIRECT_FREE -> this.branchThread = new Thread(this.prepareDirectFreeNode);
            case PREPARE_INDIRECT_FREE -> this.branchThread = new Thread(this.prepareIndirectFreeNode);
            case PREPARE_KICKOFF -> this.branchThread = new Thread(this.prepareKickoffNode);
            case PREPARE_PENALTY -> this.branchThread = new Thread(this.preparePenaltyNode);
            case NORMAL_START -> this.branchThread = new Thread(this.normalStartNode);
            case BALL_PLACEMENT -> this.branchThread = new Thread(this.ballPlacementNode);
            case FORCE_START, OPEN_PLAY -> runOpenPlay();
        }
        this.branchThread.setDaemon(true);
        this.branchThread.start();
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
     * Run correct open play node in branch thread
     */
    private void runOpenPlay() {
        if (onOffense) {
            this.branchThread = new Thread(this.offense);
        }
        else {
            this.branchThread = new Thread(this.defense);
        }
    }

}
