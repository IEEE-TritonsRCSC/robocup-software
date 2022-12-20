package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines the sequence of tasks needed to successfully perform a coordinated pass between two allies
 */
public class CoordinatedPassNode extends SequenceNode {

    private final Ally passer;

    public CoordinatedPassNode(Ally passer) {
        super("Coordinated Pass Node: " + passer.toString());
        this.passer = passer;
    }

    /**
     * Identifies best passing option
     * Sends message to central coordinator with pass details
     * Performs pass
     */
    @Override
    public NodeState execute() {
        // TODO
        return null;
    }

}
