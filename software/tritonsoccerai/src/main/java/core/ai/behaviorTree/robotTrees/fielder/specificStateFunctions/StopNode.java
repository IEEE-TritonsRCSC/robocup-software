package main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions.HaltNode;
import static proto.triton.FilteredObject.Robot;

/**
 * Handles Stop game state
 * Slows down robot to under 1.5 m/s while keeping at least
 * 0.5 m distance from ball
 */
public class StopNode extends SequenceNode {

    private final int allyID;
    private final HaltNode haltNode;

    public StopNode(int allyID, HaltNode haltNode) {
        super("Stop Node: " + allyID);
        this.allyID = allyID;
        this.haltNode = haltNode;
    }

    @Override
    public NodeState execute() {
        this.haltNode.execute();
        return NodeState.SUCCESS;
    }

}
