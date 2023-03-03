package java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import java.core.ai.GameInfo;
import java.core.ai.behaviorTree.nodes.NodeState;
import java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import java.core.util.Vector2d;

import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

public class GKPenaltyNode extends TaskNode {

    private final BlockBallNode blockBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public GKPenaltyNode() {
        super("Prepare Penalty Node : " + GameInfo.getKeeper(), GameInfo.getKeeper());
        this.blockBallNode = new BlockBallNode(GameInfo.getKeeper());
        this.moveToPositionNode = new MoveToPositionNode(GameInfo.getKeeper());
    }

    @Override
    public NodeState execute() {
        // potential improvements that can be made to this:
        // figure out in which direction shooting robot is facing
        // and move to corresponding location along goal line
        if (GameInfo.getPossessBall()) {
            while (true) {
                this.blockBallNode.execute();
            }
        }
        else {
            Vector2d centerOfGoal = new Vector2d(0, -1 * (SSL_GeometryFieldSize.getFieldLength() / 2));
            while (true) {
                this.moveToPositionNode.execute(centerOfGoal);
            }
        }
    }

}
