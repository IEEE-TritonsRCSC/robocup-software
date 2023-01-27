package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
// import proto.filtered_object.Robot;
import proto.filtered_object.Robot;

/**
 * Utilized to check if a given robot is the closest ally to the ball
 */
public class ClosestToBallNode extends ConditionalNode {

    //private final Ally ally;
    private final Robot ally;

    public ClosestToBallNode(Robot ally) {
        super("Closest To Ball Node: " + ally);
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        
        return ally == GameInfo.getAllyClosestToBall();
    }

}
