package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;

import static proto.triton.FilteredObject.Robot;

public class RobotHasPossessionNode extends ConditionalNode {

    private final Robot ally;

    public RobotHasPossessionNode(Robot ally) {
        super("Robot Has Possession Node: " + ally);
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        return ally.getHasBall();
    }

}
