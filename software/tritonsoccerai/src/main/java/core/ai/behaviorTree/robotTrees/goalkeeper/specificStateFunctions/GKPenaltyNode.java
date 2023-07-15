package main.java.core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import main.java.core.util.Vector2d;

import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

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
        if (GameInfo.getPossessBall()) {
            while (true) {
                this.blockBallNode.execute();
            }
        }
        else {
            Vector2d centerOfGoal = new Vector2d(0, -1 * (GameInfo.getField().getFieldLength() / 2));
            while (true) {
                this.moveToPositionNode.execute(centerOfGoal);
            }
        }
    }

}
