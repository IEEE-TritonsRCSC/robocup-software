package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

public class CoordinatedPassNode extends SequenceNode {

    private Ally passer;

    public CoordinatedPassNode(Ally passer) {
        super("Coordinated Pass Node: " + passer.toString());
        this.passer = passer;
    }

    @Override
    public NodeState execute() {
        return null;
    }

}
