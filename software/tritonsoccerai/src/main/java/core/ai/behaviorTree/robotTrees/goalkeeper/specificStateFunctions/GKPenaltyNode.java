package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.util.Vector2d;

import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

import static core.util.ObjectHelper.awardedBall;

public class GKPenaltyNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public GKPenaltyNode() {
        super("Prepare Penalty Node : " + 0, 0);
        this.blockBallNode = new BlockBallNode(0);
        this.moveToPositionNode = new MoveToPositionNode(0);
    }

    @Override
    public NodeState execute() {
        // potential improvements that can be made to this:
        // figure out in which direction shooting robot is facing
        // and move to corresponding location along goal line
        if (awardedBall()) {
            this.blockBallNode.setOnLine(false);
            this.blockBallNode.execute();
        }
        else {
            this.blockBallNode.setOnLine(true);
            this.blockBallNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
