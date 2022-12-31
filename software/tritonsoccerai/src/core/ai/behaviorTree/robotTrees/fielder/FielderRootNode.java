package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.DefenseRootNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.OffenseRootNode;
import core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.*;
import core.fieldObjects.robot.Ally;

/**
 * Root node of fielder tree
 * If game is in open play, plays offense or defense
 * If referee command, takes appropriate action
 * Checks for change in game status at defined frequency
 * If game status different, kills current branch execution
 * and starts execution of correct branch
 */
public class FielderRootNode extends CompositeNode {

    private final ConditionalNode haveBall;
    private final OffenseRootNode offense;
    private final DefenseRootNode defense;

    private final HaltNode haltNode;
    private final StopNode stopNode;
    private final DirectFreeNode prepareDirectFreeNode;
    private final IndirectFreeNode prepareIndirectFreeNode;
    private final KickoffNode prepareKickoffNode;
    private final PenaltyNode preparePenaltyNode;
    private final NormalStartNode normalStartNode;
    private final BallPlacementNode ballPlacementNode;

    private final ClosestToBallNode closestToBallNode;

    private GameState stateCurrentlyRunning;
    private boolean onOffense;

    private Thread branchThread;

    public FielderRootNode(Ally ally) {
        super("Fielder Root");

        this.closestToBallNode = new ClosestToBallNode(ally);

        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new OffenseRootNode(ally);
        this.defense = new DefenseRootNode(ally);

        this.haltNode = new HaltNode(ally);
        this.stopNode = new StopNode(ally);
        this.prepareDirectFreeNode = new DirectFreeNode(ally, this.closestToBallNode);
        this.prepareIndirectFreeNode = new IndirectFreeNode(ally, this.closestToBallNode);
        this.prepareKickoffNode = new KickoffNode(ally, this.closestToBallNode);
        this.preparePenaltyNode = new PenaltyNode(ally, this.closestToBallNode);
        this.normalStartNode = new NormalStartNode(ally, this.closestToBallNode);
        this.ballPlacementNode = new BallPlacementNode(ally, this.closestToBallNode);

        this.stateCurrentlyRunning = GameInfo.getCurrState();
        this.onOffense = false;

        this.branchThread = new Thread(); // this may be changed to ScheduledExecutorService
        this.branchThread.setDaemon(true);
    }

    /**
     * At a desired frequency, check if game state and ball possession has changed
     * If so, switch branch
     */
    @Override
    public NodeState execute() {
        // TODO
        if (this.stateCurrentlyRunning != GameInfo.getCurrState()) {
            switchBranch();
        }
        else if (stateCurrentlyRunning == GameState.OPEN_PLAY) {
            if (NodeState.isSuccess(this.haveBall.execute()) != this.onOffense) {
                this.onOffense = !this.onOffense;
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
            case BALL_PLACEMENT:
                this.branchThread = new Thread(this.ballPlacementNode);
                break;
            case FORCE_START:
            case OPEN_PLAY:
                runOpenPlay();
                break;
        }
        this.branchThread.setDaemon(true);
        this.branchThread.start();
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
