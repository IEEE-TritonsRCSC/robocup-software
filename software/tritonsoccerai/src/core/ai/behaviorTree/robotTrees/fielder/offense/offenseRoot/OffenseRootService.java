package core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.MakePlayNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import proto.filtered_object.Robot;

public class OffenseRootService extends ServiceNode {

    private final RobotHasPossessionNode havePossession;
    private final ConditionalNode haveOpenShot;
    private final ShootBallNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;

    public OffenseRootService(Robot ally) {
        super("Offense Root Service " + ally);
        this.havePossession = new RobotHasPossessionNode(ally);
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
