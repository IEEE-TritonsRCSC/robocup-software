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
import main.java.core.util.Vector2d;
import static proto.triton.FilteredObject.Robot;

public class OffenseRootService extends ServiceNode {

    private final RobotHasPossessionNode havePossession;
    private final HaveOpenShotNode haveOpenShot;
    private final ShootBallNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;
    // private int curr;

    public OffenseRootService(int allyID) {
        super("Offense Root Service " + allyID);
        this.havePossession = new RobotHasPossessionNode(allyID);
        this.haveOpenShot = new HaveOpenShotNode(allyID);
        this.shootBall = new ShootBallNode(allyID);
        this.makePlay = new MakePlayNode(allyID);
        this.positionSelf = new PositionSelfNode(allyID);

        // this.curr = 1;
    }

    /**
     * If ally has possession of ball, shoots or makes play
     * If ally doesn't have possession of ball, positions ally optimally
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.havePossession.execute())) {
            Vector2d shotTo = this.shootBall.findShot();
            if (shotTo != null) {
                this.shootBall.execute(shotTo);
                // System.out.println("Shoot ball executed " + curr);
            } else {
                this.makePlay.execute();
                // System.out.println("Make play executed " + curr);
            }
        }
        else {
            this.positionSelf.execute();
            // System.out.println("Position self executed " + curr);
        }
        // curr++;
        return NodeState.SUCCESS;
    }

    @Override
    public void run() {
        execute();
    }

}
