package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;

import static core.util.ObjectHelper.awardedBall;

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
            if (awardedBall()) {
                this.positionSelfNode.execute();
            } else {
                this.blockBallNode.execute();
            }
        }
    }

}
 