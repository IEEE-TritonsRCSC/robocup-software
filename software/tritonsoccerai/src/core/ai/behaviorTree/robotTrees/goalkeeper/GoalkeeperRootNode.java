package core.ai.behaviorTree.robotTrees.goalkeeper;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.GKDefenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKOffenseRootNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions.*;

// root node of goalkeeper tree
// if game is in open play, takes offensive or defensive action
// if referee command, takes appropriate action
// checks for change in game status at defined frequency
// if game status different, kills current branch execution
// and starts execution of correct branch

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
    }

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

    private void switchBranch() {
        // kill current thread
        // start new thread with executeCorrectBranch() as target
        executeCorrectBranch();
        this.stateCurrentlyRunning = GameInfo.getCurrState();
    }

    private void executeCorrectBranch() {
        switch (GameInfo.getCurrState()) {
            case OPEN_PLAY:
                if (onOffense) {
                    this.offense.execute();
                }
                else {
                    this.defense.execute();
                }
                break;
            case HALT:
                this.haltNode.execute();
                break;
            case STOP:
                this.stopNode.execute();
                break;
            case PREPARE_DIRECT_FREE:
                this.prepareDirectFreeNode.execute();
                break;
            case PREPARE_INDIRECT_FREE:
                this.prepareIndirectFreeNode.execute();
                break;
            case PREPARE_KICKOFF:
                this.prepareKickoffNode.execute();
                break;
            case PREPARE_PENALTY:
                this.preparePenaltyNode.execute();
                break;
            case NORMAL_START:
                this.normalStartNode.execute();
                break;
            case FORCE_START:
                this.forceStartNode.execute();
                break;
            case BALL_PLACEMENT:
                this.ballPlacementNode.execute();
                break;
        }
    }

}
