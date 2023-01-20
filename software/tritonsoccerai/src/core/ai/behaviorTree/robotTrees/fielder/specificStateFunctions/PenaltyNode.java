package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Handles Prepare Penalty game state
 * If our penalty, approach but do not manipulate ball if closest to ball
 * If our penalty and not closest to ball OR not our penalty,
 * stay 1 m behind ball with a tendency toward middle of field
 */
public class PenaltyNode extends TaskNode {

    private final Ally ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public PenaltyNode(Ally ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Penalty Node: " + ally.toString(), ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        float lineYValue;
        if (GameInfo.getPossessBall()) {
            lineYValue = 30;
        }
        else {
            lineYValue = -30;
        }
        if (GameInfo.getPossessBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
            while (this.ally.getPos().dist(desiredLocation) > DISTANCE_CONSTANT) {
                this.moveToPositionNode.execute(new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2));
            }
        }
        else {
            // TODO - move to a point with lineYValue
        }
        return NodeState.SUCCESS;
    }

}
