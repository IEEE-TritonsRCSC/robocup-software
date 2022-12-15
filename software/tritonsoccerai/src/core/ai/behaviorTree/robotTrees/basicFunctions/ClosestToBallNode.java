package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.fieldObjects.robot.Ally;

public class ClosestToBallNode extends ConditionalNode {

    private Ally ally;

    public ClosestToBallNode(Ally ally) {
        super("Closest To Ball Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        if (ally == GameInfo.getAllyClosestToBall()) {
            return true;
        }
        else {
            return false;
        }
    }

}
