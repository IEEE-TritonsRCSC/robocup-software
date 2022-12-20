package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.fieldObjects.robot.Ally;

/**
 * Moves ally to a suitable position behind the ball
 */
public class MoveBehindBallNode extends SequenceNode {

    private final Ally ally;

    public MoveBehindBallNode(Ally ally) {
        super("Move Behind Ball Node: " + ally.toString());
        this.ally = ally;
    }

}
