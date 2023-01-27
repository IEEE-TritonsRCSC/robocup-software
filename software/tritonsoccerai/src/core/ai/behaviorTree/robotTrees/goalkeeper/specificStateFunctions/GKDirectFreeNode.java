package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;
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
