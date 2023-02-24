package java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

public class GKNormalStartNode extends TaskNode {

    private final PositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;
    
    public GKNormalStartNode() {
        super("GK Normal Start Node: " + GameInfo.getKeeper(), GameInfo.getKeeper());
        this.positionSelfNode = new PositionSelfNode(GameInfo.getKeeper());
        this.blockBallNode = new BlockBallNode(GameInfo.getKeeper());
    }

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
 