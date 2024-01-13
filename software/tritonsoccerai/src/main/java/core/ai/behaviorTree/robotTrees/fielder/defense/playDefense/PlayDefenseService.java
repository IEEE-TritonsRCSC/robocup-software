package main.java.core.ai.behaviorTree.robotTrees.fielder.defense.playDefense;

import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import main.java.core.ai.behaviorTree.robotTrees.fielder.defense.CutPassingLaneNode;
import static proto.triton.FilteredObject.Robot;

public class PlayDefenseService extends ServiceNode {

    private final ClosestToBallNode closestToBallNode;
    private final ChaseBallNode chaseBallNode;
    private final CutPassingLaneNode cutPassingLaneNode;
    private int curr;

    public PlayDefenseService(int allyID) {
        super("Play Defense Service: " + allyID);
        this.closestToBallNode = new ClosestToBallNode(allyID);
        this.chaseBallNode = new ChaseBallNode(allyID);
        this.cutPassingLaneNode = new CutPassingLaneNode(allyID);
        this.curr = 1;
    }

    /**
     * Chases ball if ally is closest to ball
     * Otherwise, cuts a passing lane
     */
    @Override
    public NodeState execute() {
        if (NodeState.isSuccess(this.closestToBallNode.execute())) {
            this.chaseBallNode.execute();
            System.out.println("Chase ball node executed " + curr);
            curr++;
        }
        else {
            // this.cutPassingLaneNode.execute();
            System.out.println("Cut lane node executed");
        }
        return NodeState.SUCCESS;
    }

    @Override
    public void run() {
        execute();
    }

}
