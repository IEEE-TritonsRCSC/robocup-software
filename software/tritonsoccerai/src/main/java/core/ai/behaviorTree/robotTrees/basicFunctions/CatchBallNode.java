package main.java.core.ai.behaviorTree.robotTrees.basicFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;

import static proto.triton.FilteredObject.Robot;

/**
 * Defines task of catching ball in motion
 */
public class CatchBallNode extends TaskNode {

    public CatchBallNode(int allyID) {
        super("Catch Ball Node: " + allyID, allyID);
    }

    @Override
    public NodeState execute() {

        return NodeState.SUCCESS;
    }

}
