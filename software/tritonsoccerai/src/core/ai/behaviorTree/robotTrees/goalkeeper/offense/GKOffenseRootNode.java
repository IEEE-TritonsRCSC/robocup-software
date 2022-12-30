package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;

/**
 * Defines behavior for goalkeeper when on offense
 * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
 * If goalkeeper has possession, passes ball
 */
public class GKOffenseRootNode extends CompositeNode {

    // TODO
    private final ConditionalNode havePossession;
    private final PassBallNode passBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKOffenseRootNode() {
        super("GKOffense Root");
        this.havePossession = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };

        this.passBallNode = new PassBallNode();
        this.positionSelfNode = new GKPositionSelfNode();
    }

    /*
     * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
     * If goalkeeper has possession, passes ball
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.havePossession.execute())) {
            this.passBallNode.execute();
        }
        else{
            this.positionSelfNode.execute();
        }
        return NodeState.SUCCESS;
    }

    

}
