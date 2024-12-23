package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import main.java.core.constants.RobotConstants;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;

import java.util.ArrayList;
import java.util.Comparator;

import static main.java.core.constants.ProgramConstants.objectConfig;
import static main.java.core.constants.RobotConstants.DRIBBLE_THRESHOLD;

import static main.java.core.util.ProtobufUtils.getPos;
import static main.java.core.util.ObjectHelper.awardedBall;

/**
 * Handles Prepare Penalty game state
 * If our penalty, approach but do not manipulate ball if closest to ball
 * If our penalty and not closest to ball OR not our penalty,
 * stay 1 m behind ball with a tendency toward middle of field
 */
public class PenaltyNode extends TaskNode {

    private final ClosestToBallNode closestToBallNode;
    private final MoveToPositionNode moveToPositionNode;

    public PenaltyNode(int allyID, ClosestToBallNode closestToBallNode) {
        super("Prepare Penalty Node: " + allyID, allyID);
        this.closestToBallNode = closestToBallNode;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.moveToPositionNode.setAvoidBall(true);
    }

    @Override
    public NodeState execute() {
        float DISTANCE_CONSTANT = 1;
        float lineYValue;
        if (GameInfo.getPossessBall()) {
            lineYValue = 2500;
        }
        else {
            lineYValue = -2500;
        }
        if (awardedBall() && NodeState.isSuccess(this.closestToBallNode.execute())) {
            if (!GameInfo.getPossessBall(allyID)) {
                Vector2d desiredLocation = new Vector2d(GameInfo.getBall().getX(), GameInfo.getBall().getY() - DRIBBLE_THRESHOLD - objectConfig.robotRadius);
                this.moveToPositionNode.execute(desiredLocation);
            }
        }
        else {
                this.moveToPositionNode.execute(findOptimalPositionSpot(lineYValue));
        }
        return NodeState.SUCCESS;
    }

    private Vector2d findOptimalPositionSpot(float lineYValue) {
        float DISTANCE_CONSTANT = 100;
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
