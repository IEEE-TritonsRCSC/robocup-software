package java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

public class GKKickoffNode extends TaskNode {

    private final GKPositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;

    public GKKickoffNode() {
        super("Prepare Kickoff Node : " + GameInfo.getKeeper(), GameInfo.getKeeper());
        this.positionSelfNode = new GKPositionSelfNode(null);
        this.blockBallNode = new BlockBallNode(GameInfo.getKeeper());
    }

    @Override
    public NodeState execute() {
        while (true) {
            if (GameInfo.getPossessBall()) {
                this.positionSelfNode.execute();
            } else {
                this.blockBallNode.execute();
            }
        }
    }

}
