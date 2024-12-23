package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.ai.GameInfo;

import static proto.triton.FilteredObject.Robot;

public class RobotHasPossessionNode extends ConditionalNode {

    private final int allyID;

    public RobotHasPossessionNode(int allyID) {
        super("Robot Has Possession Node: " + allyID);
        this.allyID = allyID;
    }

    @Override
    public boolean conditionSatisfied() {
        return GameInfo.getPossessBall(allyID);
    }

}
