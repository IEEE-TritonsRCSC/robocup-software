package core.ai.behaviorTree.robotTrees.fielder.fielderRoot;

import core.ai.GameInfo;
import core.constants.ProgramConstants;
// import core.ai.GameState;
import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.playDefense.PlayDefenseService;
import core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot.OffenseRootService;
import core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.*;
import static proto.triton.FilteredObject.Robot;
import static proto.gc.SslGcRefereeMessage.Referee;

import static core.util.ProtobufUtils.getPos;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Checks for change in game status
 * If game status different, kills current branch execution
 * and starts execution of correct branch
 */
public class FielderRootService extends ServiceNode {

    private final ScheduledThreadPoolExecutor executor;

    private final ConditionalNode haveBall;
    private final OffenseRootService offense;
    private final PlayDefenseService defense;

    private final HaltNode haltNode;
    private final StopNode stopNode;
    private final DirectFreeNode prepareDirectFreeNode;
    private final IndirectFreeNode prepareIndirectFreeNode;
    private final KickoffNode prepareKickoffNode;
    private final PenaltyNode preparePenaltyNode;
    private final NormalStartNode normalStartNode;
    private final BallPlacementNode ballPlacementNode;

    private final ClosestToBallNode closestToBallNode;

    private Referee.Command commandCurrentlyRunning;
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
        this.offense = new OffenseRootService(allyID);
        this.defense = new PlayDefenseService(allyID);

        this.haltNode = new HaltNode(allyID);
        this.stopNode = new StopNode(allyID, this.haltNode);
        this.prepareDirectFreeNode = new DirectFreeNode(allyID, this.closestToBallNode);
        this.prepareIndirectFreeNode = new IndirectFreeNode(allyID, this.closestToBallNode);
        this.prepareKickoffNode = new KickoffNode(allyID, this.closestToBallNode);
        this.preparePenaltyNode = new PenaltyNode(allyID, this.closestToBallNode);
        this.normalStartNode = new NormalStartNode(allyID, this.closestToBallNode);
        this.ballPlacementNode = new BallPlacementNode(allyID, this.closestToBallNode);

        this.onOffense = false;
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
        // System.out.println(getPos(GameInfo.getAlly(allyID)).dist(getPos(GameInfo.getBall())));
        return NodeState.SUCCESS;
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
                // System.out.println("Ally " + allyID + " is running halt node");
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
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);;
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
                this.branchFuture = this.executor.scheduleAtFixedRate(this.ballPlacementNode, ProgramConstants.INITIAL_DELAY,
                                                                    ProgramConstants.LOOP_DELAY, TimeUnit.MILLISECONDS);
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
     * Run correct open play node in branch thread
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
