package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import static proto.triton.FilteredObject.Robot;

/**
 * Utilized to check if a given robot is the closest ally to the ball
 */
public class ClosestToBallNode extends ConditionalNode {

    //private final Ally ally;
    private final int allyID;

    public ClosestToBallNode(int allyID) {
        super("Closest To Ball Node: " + allyID);
        this.allyID = allyID;
    }

    @Override
    public boolean conditionSatisfied() {
        return allyID == GameInfo.getAllyClosestToBall().getId();
    }

}
