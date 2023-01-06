package core.ai.behaviorTree.robotTrees.basicFunctions;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.fieldObjects.robot.Ally;

/**
 * Defines tasks to be performed to dribble ball
 */
public class DribbleBallNode extends TaskNode {

    private final Ally ally;

    // TODO Pathfinding algorithms needed
    public DribbleBallNode(Ally ally) {
        // TODO
        super("Dribble Ball Node: " + ally.toString(), ally);
        this.ally = ally;
    }

    /**
     * Move into ball at a speed that keeps the ball close to robot
     */
    @Override
    public NodeState execute() {
        // TODO Pathfinding algorithms needed

        return null;
    }

}
