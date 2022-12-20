package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.BTNode;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines the behavior for playing defense as a fielder
 * Moves ally behind ball, then either chases ball or cuts a passing lane
 */
public class DefenseRootNode extends SequenceNode {

    private final MoveBehindBallNode moveBehindBallNode;
    private final PlayDefenseNode playDefenseNode;

    public DefenseRootNode(Ally ally) {
        super("Defense Root Node");
        this.moveBehindBallNode = new MoveBehindBallNode(ally);
        this.playDefenseNode = new PlayDefenseNode(ally);
        this.sequence = new BTNode[]{this.moveBehindBallNode, this.playDefenseNode};
    }

}
