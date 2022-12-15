package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

public class DefenseRootNode extends SequenceNode {

    private MoveBehindBallNode moveBehindBallNode;
    private PlayDefenseNode playDefenseNode;

    public DefenseRootNode(Ally ally) {
        super("Defense Root Node");
        this.moveBehindBallNode = new MoveBehindBallNode(ally);
        this.playDefenseNode = new PlayDefenseNode(ally);
        this.sequence = new BTNode[]{this.moveBehindBallNode, this.playDefenseNode};
    }

}
