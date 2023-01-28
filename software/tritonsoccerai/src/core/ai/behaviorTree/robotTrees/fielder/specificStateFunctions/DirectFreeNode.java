package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.fielder.offense.PositionSelfNode;
import proto.filtered_object.Robot;
import core.util.Vector2d;

import static core.util.ProtobufUtils.getPos;

/**
 * Handles Prepare Direct Free game state
 */
public class DirectFreeNode extends TaskNode {

    private final Robot ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private final PositionSelfNode positionSelfNode;

    public DirectFreeNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Ball Placement Node: " + ally, ally);
        this.ally = ally;
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(ally);
        this.positionSelfNode = new PositionSelfNode(ally);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        if (GameInfo.getPossessBall()) {
            if (NodeState.isSuccess(this.closestToBallNode.execute())) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - 2);
                while (getPos(this.ally).dist(desiredLocation) > DISTANCE_CONSTANT) {
                    this.moveToPositionNode.execute(desiredLocation);
                }
            }
            else {
                while (true) {
                    this.positionSelfNode.execute();
                }
            }
        }
        else {
            // TODO - make a wall and guard foes
        }
        return NodeState.SUCCESS;
    }

}
