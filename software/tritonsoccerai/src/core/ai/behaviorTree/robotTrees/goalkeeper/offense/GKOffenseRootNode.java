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

    public GKOffenseRootNode() {
        super("GKOffense Root");
        this.havePossession = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return GameInfo.getPossessBall();
            }
        };
    }

    /*
     * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
     * If goalkeeper has possession, passes ball
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.havePossession.execute())) {

        }
        else{

        }
        return NodeState.SUCCESS;
    }

    

}
