package core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Handles Halt game state
 * Stops robot within 2 seconds without manipulating ball
 */
public class HaltNode extends SequenceNode {

    private final Ally ally;
    private final MoveToPositionNode moveToPositionNode;

    public HaltNode(Ally ally) {
        super("Halt Node: " + ally.toString());
        this.ally = ally;
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        // TODO stop the robot's motion
        this.moveToPositionNode.execute(new Vector2d(ally.getX(), ally.getY()));
        return NodeState.SUCCESS;
    }

}
