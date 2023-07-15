package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

public class GKNormalStartNode extends TaskNode {

    private final PositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;
    
    public GKNormalStartNode() {
        super("GK Normal Start Node: " + 0, 0);
        this.positionSelfNode = new PositionSelfNode(0);
        this.blockBallNode = new BlockBallNode(0);
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
 