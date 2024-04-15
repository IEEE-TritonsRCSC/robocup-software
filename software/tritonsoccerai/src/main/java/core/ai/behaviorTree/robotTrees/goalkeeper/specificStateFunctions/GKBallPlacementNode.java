package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

import static main.java.core.util.ObjectHelper.awardedBall;

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
