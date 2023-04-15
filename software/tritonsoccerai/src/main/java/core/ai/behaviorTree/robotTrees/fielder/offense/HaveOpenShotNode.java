package main.java.core.ai.behaviorTree.robotTrees.fielder.offense;

import main.java.core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;
import static proto.triton.FilteredObject.Robot;

public class HaveOpenShotNode extends ConditionalNode {

    private final Robot ally;

    public HaveOpenShotNode(Robot ally) {
        this.ally = ally;
    }

    @Override
    public boolean conditionSatisfied() {
        // TODO
        return false;
    }

}