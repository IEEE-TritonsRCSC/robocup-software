package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

public class GKDirectFreeNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKDirectFreeNode() {
            super("Ball Placement Node: " + 0, 0);
            this.blockBallNode = new BlockBallNode(0);
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
