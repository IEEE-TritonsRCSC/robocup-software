package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

import static core.util.ObjectHelper.awardedBall;

public class GKBallPlacementNode extends TaskNode {

    private final GKPositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;

    public GKBallPlacementNode() {
        super("GK Ball Placement: " + 0, 0);
        this.positionSelfNode = new GKPositionSelfNode();
        this.blockBallNode = new BlockBallNode(0);
    }

    @Override
    public NodeState execute() {
        while (true) {
            if (awardedBall()) {
                this.positionSelfNode.execute();
            } else {
                this.blockBallNode.execute();
            }
        }
    }

}
