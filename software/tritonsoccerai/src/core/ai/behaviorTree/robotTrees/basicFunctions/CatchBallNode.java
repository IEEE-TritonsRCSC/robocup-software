package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines task of catching ball in motion
 */
public class CatchBallNode extends TaskNode {

    public CatchBallNode(Ally ally) {
        super("Catch Ball Node: " + ally.toString(), ally);
    }

    @Override
    public NodeState execute() {
        // TODO
        return NodeState.SUCCESS;
    }

}
