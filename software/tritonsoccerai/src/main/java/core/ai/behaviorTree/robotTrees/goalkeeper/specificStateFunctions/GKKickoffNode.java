package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;

public class GKKickoffNode extends TaskNode {

    private final GKPositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;

    public GKKickoffNode() {
        super("Prepare Kickoff Node : " + 0, 0);
        this.positionSelfNode = new GKPositionSelfNode();
        this.blockBallNode = new BlockBallNode(0);
    }

    @Override
    public NodeState execute() {
        this.blockBallNode.execute();
        return NodeState.SUCCESS;
    }

}
