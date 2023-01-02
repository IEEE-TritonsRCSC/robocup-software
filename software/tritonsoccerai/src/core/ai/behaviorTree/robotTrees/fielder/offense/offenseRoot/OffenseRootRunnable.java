package core.ai.behaviorTree.robotTrees.fielder.offense.offenseRoot;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.MakePlayNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.ShootBallNode;
import core.fieldObjects.robot.Ally;

public class OffenseRootRunnable implements Runnable {

    private final ConditionalNode havePossession;
    private final ConditionalNode haveOpenShot;
    private final TaskNode shootBall;
    private final CompositeNode makePlay;
    private final PositionSelfNode positionSelf;

    public OffenseRootRunnable(Ally ally) {
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
    public void run() {
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
    }

}
