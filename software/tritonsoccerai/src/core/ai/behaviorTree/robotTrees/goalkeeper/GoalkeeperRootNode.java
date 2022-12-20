package core.ai.behaviorTree.robotTrees.goalkeeper;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.GKDefenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKOffenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.*;

/**
 * root node of goalkeeper tree
 * if game is in open play, takes offensive or defensive action
 * if referee command, takes appropriate action
 * checks for change in game status at defined frequency
 * if game status different, kills current branch execution
 * and starts execution of correct branch
 */
public class GoalkeeperRootNode extends CompositeNode {

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
    private final GKForceStartNode forceStartNode;
    private final GKBallPlacementNode ballPlacementNode;

    private GameState stateCurrentlyRunning;
    private boolean onOffense;

    private Thread branchThread;

    public GoalkeeperRootNode() {
        super("Goalkeeper Root");
        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new GKOffenseRootNode();
        this.defense = new GKDefenseRootNode();

        this.haltNode = new GKHaltNode();
        this.stopNode = new GKStopNode();
        this.prepareDirectFreeNode = new GKDirectFreeNode();
        this.prepareIndirectFreeNode = new GKIndirectFreeNode();
        this.prepareKickoffNode = new GKKickoffNode();
        this.preparePenaltyNode = new GKPenaltyNode();
        this.normalStartNode = new GKNormalStartNode();
        this.forceStartNode = new GKForceStartNode();
        this.ballPlacementNode = new GKBallPlacementNode();

        this.stateCurrentlyRunning = GameInfo.getCurrState();
        this.onOffense = false;

        this.branchThread = new Thread();
        this.branchThread.setDaemon(true);
    }

    /**
     * At a desired frequency, check if game state and ball possession has changed
     * If so, switch branch
     */
    @Override
    public NodeState execute() {
        // TODO
        // at a desired frequency, check if game state and ball possession has changed
        // if so, switch branch
        if (this.stateCurrentlyRunning != GameInfo.getCurrState()) {
            switchBranch();
        }
        else if (stateCurrentlyRunning == GameState.OPEN_PLAY) {
            if (NodeState.isSuccess(this.haveBall.execute()) != this.onOffense) {
                switchBranch();
            }
        }
        return NodeState.RUNNING;
    }

    /**
     * Kill execution of currently-executing branch
     * Execute the correct branch
     */
    private void switchBranch() {
        // kill current thread
        // TODO interrupt() doesn't actually stop execution of the target
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
            case HALT:
                this.branchThread = new Thread(this.haltNode);
                break;
            case STOP:
                this.branchThread = new Thread(this.stopNode);
                break;
            case PREPARE_DIRECT_FREE:
                this.branchThread = new Thread(this.prepareDirectFreeNode);
                break;
            case PREPARE_INDIRECT_FREE:
                this.branchThread = new Thread(this.prepareIndirectFreeNode);
                break;
            case PREPARE_KICKOFF:
                this.branchThread = new Thread(this.prepareKickoffNode);
                break;
            case PREPARE_PENALTY:
                this.branchThread = new Thread(this.preparePenaltyNode);
                break;
            case NORMAL_START:
                this.branchThread = new Thread(this.normalStartNode);
                break;
            case FORCE_START:
                this.branchThread = new Thread(this.forceStartNode);
                break;
            case BALL_PLACEMENT:
                this.branchThread = new Thread(this.ballPlacementNode);
                break;
            case OPEN_PLAY:
                if (onOffense) {
                    this.branchThread = new Thread(this.offense);
                }
                else {
                    this.branchThread = new Thread(this.defense);
                }
                break;
        }
        this.branchThread.setDaemon(true);
        this.branchThread.start();
    }

}
