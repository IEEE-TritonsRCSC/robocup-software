package software.tritonsoccerai.src.core.ai.behaviorTree.robotTrees.fielder.specificStateFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import static proto.triton.FilteredObject.Robot;
import core.util.Vector2d;

import static core.util.ProtobufUtils.getPos;

/**
 * Handles Halt game state
 * Stops robot within 2 seconds without manipulating ball
 */
public class HaltNode extends SequenceNode {

    private final Robot ally;
    private final MoveToPositionNode moveToPositionNode;

    public HaltNode(Robot ally) {
        super("Halt Node: " + ally);
        this.ally = ally;
        this.moveToPositionNode = new MoveToPositionNode(ally);
    }

    @Override
    public NodeState execute() {
        // move to current location until moving slow enough
        float MAX_VEL_CONSTANT = 2.0f;
        while(new Vector2d(ally.getVx(), ally.getVy()).mag() < MAX_VEL_CONSTANT) {
            this.moveToPositionNode.execute(getPos(ally));
        }
        return NodeState.SUCCESS;
    }

}
