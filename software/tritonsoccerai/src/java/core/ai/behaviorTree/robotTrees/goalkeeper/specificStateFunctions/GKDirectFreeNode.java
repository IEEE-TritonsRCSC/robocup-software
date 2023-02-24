package java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

public class GKDirectFreeNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKDirectFreeNode() {
            super("Ball Placement Node: " + GameInfo.getKeeper(), GameInfo.getKeeper());
            this.blockBallNode = new BlockBallNode(GameInfo.getKeeper());
            this.positionSelfNode = new GKPositionSelfNode(null);
        }

    @Override
    public NodeState execute() {
        while (true) {
            if (GameInfo.getPossessBall()) {
                this.positionSelfNode.execute(); // set up GK on offense
            } else {
                this.blockBallNode.execute(); // set up GK on defense
            }
        }
    }

}
