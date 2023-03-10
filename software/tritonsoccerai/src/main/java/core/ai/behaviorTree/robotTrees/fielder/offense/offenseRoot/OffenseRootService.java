package main.java.core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.MakePlayNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.HaveOpenShotNode;
import static proto.triton.FilteredObject.Robot;

public class OffenseRootService extends ServiceNode {

    private final RobotHasPossessionNode havePossession;
    private final HaveOpenShotNode haveOpenShot;
    private final ShootBallNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;

    public OffenseRootService(Robot ally) {
        super("Offense Root Service " + ally);
        this.havePossession = new RobotHasPossessionNode(ally);
        this.haveOpenShot = new HaveOpenShotNode(ally);
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
