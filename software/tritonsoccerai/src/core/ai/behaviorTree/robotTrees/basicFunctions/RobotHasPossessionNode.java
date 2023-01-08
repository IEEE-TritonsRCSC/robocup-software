package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.fieldObjects.robot.Ally;

public class RobotHasPossessionNode extends ConditionalNode {

    private final Ally ally;

    public RobotHasPossessionNode(Ally ally) {
        super("Robot Has Possession Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        return GameInfo.getBall().getPossessedBy() == this.ally;
    }

}
