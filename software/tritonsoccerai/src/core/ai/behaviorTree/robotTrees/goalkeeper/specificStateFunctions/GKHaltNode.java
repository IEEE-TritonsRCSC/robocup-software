package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.NodeState;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

public class GKHaltNode extends BTNode {

    private final Ally ally;


    public GKHaltNode(Ally ally) {
        super("GK Halt Node: " + ally.toString());
        this.ally = ally;
    }

    @Override
    public NodeState execute() {
        ally.setVel(new Vector2d(0, 0)); // set velocity to 0 to stop? 
        return NodeState.SUCCESS;
    }

}
