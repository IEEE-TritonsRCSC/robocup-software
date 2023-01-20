package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.GameState;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.fieldObjects.robot.Ally;

public class GKNormalStartNode extends SequenceNode {

    private final Ally ally;
    private final CoordinatedPassNode coordinatedPassNode;
    
    public GKNormalStartNode(Ally ally) {
        super("GK Normal Start Node: " + ally.toString());
        this.ally = ally;
        this.coordinatedPassNode = new CoordinatedPassNode(ally);
    }

    public NodeState execute() {
        // safe to assume GK will always be passing?
        if(GameInfo.getPossessBall()) {
            this.coordinatedPassNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
 