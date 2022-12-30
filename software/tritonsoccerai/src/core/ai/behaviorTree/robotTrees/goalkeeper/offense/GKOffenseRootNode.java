package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines behavior for goalkeeper when on offense
 * If goalkeeper doesn't have possession, moves to optimal position to be a passing option
 * If goalkeeper has possession, passes ball
 */
public class GKOffenseRootNode extends CompositeNode {

    private final ConditionalNode havePossession;
    private final ConditionalNode passingOption;
    private final TaskNode passBall;
    private final TaskNode moveOpenPass;
    // TODO

    public GKOffenseRootNode() {
        super("GK Offense Root");
        this.havePossession = new ConditionalNode() {

            @Override
            public boolean conditionSatisfied() {
                // TODO not sure how to make sure goalie has possession
                return true; //ally.getHasPossession();
            }
        };

        this.passingOption = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                // TODO Find way to determine if passing is an option at current location to any ally 
                return true;
            }
        };

        this.passBall = new PassBallNode();
        this.moveOpenPass = new OpenPassNode();
    }

    @Override
    public NodeState execute() {
        if(NodeState.isSuccess(this.havePossession.execute())) {
            if(NodeState.isSuccess(this.passingOption.execute())) {
                this.passBall.execute();
            }
            else {
                this.moveOpenPass.execute();
            }
        }
        else {
            // TODO unsure of what to do if doesn't have possession and on offense
        }
        return null;
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
