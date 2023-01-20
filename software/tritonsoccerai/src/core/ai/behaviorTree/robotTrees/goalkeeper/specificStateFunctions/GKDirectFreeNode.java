package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

public class GKDirectFreeNode extends SequenceNode {

    private final Ally ally;
    private final BlockBallNode blockBallNode;
    private final GKPositionSelfNode positionSelfNode;

    public GKDirectFreeNode(Ally ally) {
            super("Ball Placement Node: " + ally.toString());
            this.ally = ally;
            this.blockBallNode = new BlockBallNode(ally);
            this.positionSelfNode = new GKPositionSelfNode(ally, null);
        }

    @Override
    public NodeState execute() {
        if (GameInfo.getPossessBall()) {
            this.positionSelfNode.execute(); // set up GK on offense
        } else {
            this.blockBallNode.execute(); // set up GK on defense
        }
        return NodeState.SUCCESS;
    }

}
