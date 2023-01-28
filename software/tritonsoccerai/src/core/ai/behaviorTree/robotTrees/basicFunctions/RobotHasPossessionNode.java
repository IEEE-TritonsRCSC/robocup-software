package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
//import proto.filtered_object.Robot;

import proto.filtered_object.Robot;

public class RobotHasPossessionNode extends ConditionalNode {

    private final Robot ally;

    public RobotHasPossessionNode(Robot ally) {
        super("Robot Has Possession Node: " + ally);
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        return ally.hasBall();
    }

}
