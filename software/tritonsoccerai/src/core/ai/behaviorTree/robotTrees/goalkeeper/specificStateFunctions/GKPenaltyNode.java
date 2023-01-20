package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;
import core.fieldObjects.robot.Ally;

public class GKPenaltyNode extends SequenceNode {

    private final Ally ally;
    private final BlockBallNode blockBallNode;

    public GKPenaltyNode(Ally ally) {
        super("Prepare Penalty Node : " + ally.toString());
        this.ally = ally;
        this.blockBallNode = new BlockBallNode(ally);
    }

    @Override
    public NodeState execute() {
        if(!GameInfo.getPossessBall()) {
            this.blockBallNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
