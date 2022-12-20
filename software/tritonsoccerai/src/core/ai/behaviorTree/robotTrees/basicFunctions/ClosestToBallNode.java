package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import core.fieldObjects.robot.Ally;

/**
 * Utilized to check if a given robot is the closest ally to the ball
 */
public class ClosestToBallNode extends ConditionalNode {

    private final Ally ally;

    public ClosestToBallNode(Ally ally) {
        super("Closest To Ball Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        return ally == GameInfo.getAllyClosestToBall();
    }

}
