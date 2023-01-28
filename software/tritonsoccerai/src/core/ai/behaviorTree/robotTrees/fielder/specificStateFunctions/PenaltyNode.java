package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.constants.RobotConstants;
import proto.filtered_object.Robot;
import core.util.Vector2d;

import java.util.ArrayList;
import java.util.Comparator;

import static core.util.ProtobufUtils.getPos;

/**
 * Handles Prepare Penalty game state
 * If our penalty, approach but do not manipulate ball if closest to ball
 * If our penalty and not closest to ball OR not our penalty,
 * stay 1 m behind ball with a tendency toward middle of field
 */
public class PenaltyNode extends TaskNode {

    private final Robot ally;
    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public PenaltyNode(Robot ally, ClosestToBallNode closestToBallNode) {
        super("Prepare Penalty Node: " + ally, ally);
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
            while (getPos(this.ally).dist(desiredLocation) > DISTANCE_CONSTANT) {
                this.moveToPositionNode.execute(desiredLocation);
            }
        }
        else {
            while ((this.ally.getY() - lineYValue) > DISTANCE_CONSTANT) {
                this.moveToPositionNode.execute(findOptimalPositionSpot(lineYValue));
            }
        }
        return NodeState.SUCCESS;
    }

    private Vector2d findOptimalPositionSpot(float lineYValue) {
        float DISTANCE_CONSTANT = 1;
        float SPACE_CONSTANT = (float) (1.5 * RobotConstants.ROBOT_WIDTH);
        ArrayList<Double> xVals = new ArrayList<>();
        for (Robot robot : GameInfo.getFielders()) {
            if (Math.abs(robot.getY() - lineYValue) < DISTANCE_CONSTANT) {
                xVals.add((double) robot.getX());
            }
        }
        for (Robot robot : GameInfo.getFoeFielders()) {
            if (Math.abs(robot.getY() - lineYValue) < DISTANCE_CONSTANT) {
                xVals.add((double) robot.getX());
            }
        }
        xVals.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return (int) (o1 - o2);
            }
        });
        float bestX = 10;
        for (int i = 1; i < xVals.size(); i++) {
            if (xVals.get(i) - xVals.get(i - 1) > SPACE_CONSTANT) {
                float spotCenter = (float) (xVals.get(i) - ((xVals.get(i) - xVals.get(i - 1)) / 2));
                if (Math.abs(spotCenter) < Math.abs(bestX)) {
                    bestX = spotCenter;
                }
            }
        }
        return new Vector2d(bestX, lineYValue);
    }

}
