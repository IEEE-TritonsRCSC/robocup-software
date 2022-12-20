package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines behavior for playing offense as a fielder
 * If ally has possession of ball, shoots or makes play
 * If ally doesn't have possession of ball, positions ally optimally
 */
public class OffenseRootNode extends CompositeNode {

    private final ConditionalNode havePossession;
    private final ConditionalNode haveOpenShot;
    private final TaskNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;

    public OffenseRootNode(Ally ally) {
        super("Offense Root");
        this.havePossession = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                return ally.getHasPossession();
            }
        };
        this.haveOpenShot = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                // TODO
                // determine if there is an open shot using available coordinate info
                return true;
            }
        };
        this.shootBall = new ShootBallNode(ally);
        this.makePlay = new MakePlayNode(ally);
        this.positionSelf = new PositionSelfNode(ally);
    }

    /**
     * If ally has possession of ball, shoots or makes play
     * If ally doesn't have possession of ball, positions ally optimally
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.havePossession.execute())) {
            if (NodeState.isSuccess(this.haveOpenShot.execute())) {
                this.shootBall.execute();
            } else {
                this.makePlay.execute();
            }
        }
        else {
            this.positionSelf.execute();
        }
        return NodeState.SUCCESS;
    }

}
