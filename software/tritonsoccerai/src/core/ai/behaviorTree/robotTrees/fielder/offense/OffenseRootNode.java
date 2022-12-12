package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MakePlayNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ShootBallNode;

public class OffenseRootNode extends CompositeNode {

    private ConditionalNode haveOpenShot;
    private SequenceNode shootBall;
    private CompositeNode makePlay;

    public OffenseRootNode() {
        super("Offense Root");
        this.haveOpenShot = new ConditionalNode() {
            @Override
            public boolean conditionSatisfied() {
                // TODO
                // determine if there is an open shot using available coordinate info
                return true;
            }
        };
        this.shootBall = new ShootBallNode();
        this.makePlay = new MakePlayNode();
    }

    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.haveOpenShot.execute())) {
            this.shootBall.execute();
        }
        else {
            this.makePlay.execute();
        }
        return NodeState.SUCCESS;
    }

}
