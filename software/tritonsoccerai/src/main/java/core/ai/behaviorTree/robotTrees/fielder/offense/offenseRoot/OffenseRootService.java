package core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.RobotHasPossessionNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CatchBallNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.MakePlayNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.HaveOpenShotNode;
import core.util.Vector2d;
import static proto.triton.FilteredObject.Robot;
import core.ai.GameInfo;

public class OffenseRootService extends ServiceNode {

    private final RobotHasPossessionNode havePossession;
    private final HaveOpenShotNode haveOpenShot;
    private final ShootBallNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;
    private final CatchBallNode catchBall;
    private int allyID;
    // private int curr;

    public OffenseRootService(int allyID) {
        super("Offense Root Service " + allyID);
        this.allyID = allyID;
        this.havePossession = new RobotHasPossessionNode(allyID);
        this.haveOpenShot = new HaveOpenShotNode(allyID);
        this.shootBall = new ShootBallNode(allyID);
        this.makePlay = new MakePlayNode(allyID);
        this.positionSelf = new PositionSelfNode(allyID);
        this.catchBall = new CatchBallNode(allyID);

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
        } else if(GameInfo.getCoordinatedPass().getReceiverID() == this.allyID) {
            this.catchBall.execute();
            if (allyID == 2) {System.out.println("Catch ball executed " + allyID);}
        } else {
            this.positionSelf.execute();
            if (allyID == 2) {System.out.println("Position self executed " + allyID);}
        }
        // curr++;
        return NodeState.SUCCESS;
    }

    @Override
    public void run() {
        execute();
    }

}
