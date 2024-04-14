package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

import static main.java.core.util.ObjectHelper.awardedBall;

public class GKIndirectFreeNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKIndirectFreeNode() {
        super("GK Indirect Free Node: " + 0, 0);
        this.blockBallNode = new BlockBallNode(0);
        this.positionSelfNode = new GKPositionSelfNode(null);
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
