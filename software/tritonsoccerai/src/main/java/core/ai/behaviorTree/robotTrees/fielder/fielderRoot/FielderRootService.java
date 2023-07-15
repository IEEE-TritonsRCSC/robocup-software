package main.java.core.ai.behaviorTree.robotTrees.fielder.fielderRoot;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import main.java.core.ai.behaviorTree.nodes.BTNode;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.defense.playDefense.PlayDefenseNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot.OffenseRootNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.*;
import static proto.triton.FilteredObject.Robot;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Checks for change in game status
 * If game status different, kills current branch execution
 * and starts execution of correct branch
 */
public class FielderRootService extends ServiceNode {

    private final ScheduledThreadPoolExecutor executor;

    private final ConditionalNode haveBall;
    private final OffenseRootNode offense;
    private final PlayDefenseNode defense;

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

    private Future<?> branchFuture;
    private BTNode currentlyExecutingNode;

    private int allyID;

    public FielderRootService(int allyID, ScheduledThreadPoolExecutor executor) {
        super("Fielder Root Service: " + allyID);

        this.executor = executor;
        this.allyID = allyID;
        this.closestToBallNode = new ClosestToBallNode(allyID);

        this.haveBall = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
        this.offense = new OffenseRootNode(allyID, executor);
        this.defense = new PlayDefenseNode(allyID, executor);

        this.haltNode = new HaltNode(allyID);
        this.stopNode = new StopNode(allyID, this.haltNode);
        this.prepareDirectFreeNode = new DirectFreeNode(allyID, this.closestToBallNode);
        this.prepareIndirectFreeNode = new IndirectFreeNode(allyID, this.closestToBallNode);
        this.prepareKickoffNode = new KickoffNode(allyID, this.closestToBallNode);
        this.preparePenaltyNode = new PenaltyNode(allyID, this.closestToBallNode);
        this.normalStartNode = new NormalStartNode(allyID, this.closestToBallNode);
        this.ballPlacementNode = new BallPlacementNode(allyID, this.closestToBallNode);

        this.stateCurrentlyRunning = GameState.OPEN_PLAY;
        this.onOffense = false;
        runOpenPlay();
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
     * Kill execution of currently-executing branch
     * Execute the correct branch
     */
    private void switchBranch() {
        // kill current thread
        this.currentlyExecutingNode.stopExecution();
        this.branchFuture.cancel(true);
        // start new thread with executeCorrectBranch()
        executeCorrectBranch();
        this.stateCurrentlyRunning = GameInfo.getCurrState();
        System.out.println("Ally " + allyID + " switched branches");
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
     * Run correct open play node in branch thread
     */
    private void runOpenPlay() {
        System.out.println("running open play");
        if (onOffense) {
            System.out.println("running offense");
            this.branchFuture = this.executor.submit(this.offense);
            this.currentlyExecutingNode = this.offense;
        }
        else {
            System.out.println("running defense");
            this.branchFuture = this.executor.submit(this.defense);
            this.currentlyExecutingNode = this.defense;
        }
    }

}
