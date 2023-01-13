package core.ai.behaviorTree.robotTrees.fielder.defense.playDefense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.fielder.defense.CutPassingLaneNode;
import core.fieldObjects.robot.Ally;

public class PlayDefenseService extends ServiceNode {

    private final ClosestToBallNode closestToBallNode;
    private final ChaseBallNode chaseBallNode;
    private final CutPassingLaneNode cutPassingLaneNode;

    public PlayDefenseService(Ally ally) {
        super("Play Defense Service: " + ally);
        this.closestToBallNode = new ClosestToBallNode(ally);
        this.chaseBallNode = new ChaseBallNode(ally);
        this.cutPassingLaneNode = new CutPassingLaneNode(ally);
    }

    /**
     * Chases ball if ally is closest to ball
     * Otherwise, cuts a passing lane
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.closestToBallNode.execute())) {
            this.chaseBallNode.execute();
        }
        else {
            this.cutPassingLaneNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
