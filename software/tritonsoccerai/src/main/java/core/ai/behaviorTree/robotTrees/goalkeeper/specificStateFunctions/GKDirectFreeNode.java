package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

import static core.util.ObjectHelper.awardedBall;

public class GKDirectFreeNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKDirectFreeNode() {
        super("Ball Placement Node: " + 0, 0);
        this.blockBallNode = new BlockBallNode(0);
        this.positionSelfNode = new GKPositionSelfNode();
    }

    @Override
    public NodeState execute() {
        if (awardedBall()) {
            this.positionSelfNode.execute(); // set up GK on offense
        } else {
            this.blockBallNode.execute(); // set up GK on defense
        }
        return NodeState.SUCCESS;
    }

}
