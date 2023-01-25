package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
//import core.fieldObjects.robot.Ally;

import proto.filtered_object.Robot;

public class RobotHasPossessionNode extends ConditionalNode {

    private final Robot ally;

    public RobotHasPossessionNode(Robot ally) {
        super("Robot Has Possession Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        return GameInfo.getBall().getPossessedBy() == this.ally;
    }

}
