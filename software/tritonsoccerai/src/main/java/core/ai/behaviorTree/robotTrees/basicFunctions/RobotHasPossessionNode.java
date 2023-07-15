package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import main.java.core.ai.GameInfo;

import static proto.triton.FilteredObject.Robot;

public class RobotHasPossessionNode extends ConditionalNode {

    private final int allyID;

    public RobotHasPossessionNode(int allyID) {
        super("Robot Has Possession Node: " + allyID);
        this.allyID = allyID;
    }

    @Override
    public boolean conditionSatisfied() {
        return GameInfo.getAlly(allyID).getHasBall();
    }

}
