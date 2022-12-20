package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;

/**
 * At defined frequency, either chases ball or cuts a passing lane
 */
public class PlayDefenseNode extends CompositeNode {

    private final ClosestToBallNode closestToBallNode;
    private final ChaseBallNode chaseBallNode;
    private final CutPassingLaneNode cutPassingLaneNode;

    public PlayDefenseNode(Ally ally) {
        super("Play Defense Node: " + ally.toString());
        this.closestToBallNode = new ClosestToBallNode(ally);
        this.chaseBallNode = new ChaseBallNode(ally);
        this.cutPassingLaneNode = new CutPassingLaneNode(ally);
    }

    /**
     * At defined frequency, takes the appropriate defensive action
     */
    @Override
    public NodeState execute() {
        // TODO
        // at desired frequency, run playDefense()
        return NodeState.SUCCESS;
    }

    /**
     * Chases ball if ally is closest to ball
     * Otherwise, cuts a passing lane
     */
    private void playDefense() {
        if (NodeState.isSuccess(this.closestToBallNode.execute())) {
            this.chaseBallNode.execute();
        }
        else {
            this.cutPassingLaneNode.execute();
        }
    }

}
