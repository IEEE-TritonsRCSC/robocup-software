package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.util.Vector2d;

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
            Vector2d centerOfGoal = new Vector2d(0, -1 * (SSL_GeometryFieldSize.fieldLength / 2));
            while (true) {
                this.moveToPositionNode.execute(centerOfGoal);
            }
        }
    }

}
