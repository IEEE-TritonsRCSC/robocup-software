package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.fieldObjects.robot.Ally;
import org.w3c.dom.Node;

public class PlayDefenseNode extends CompositeNode {

    private ClosestToBallNode closestToBallNode;
    private ChaseBallNode chaseBallNode;
    private CutPassingLaneNode cutPassingLaneNode;
    private boolean chasingBall;
    private Thread branchThread;

    public PlayDefenseNode(Ally ally) {
        super("Play Defense Node: " + ally.toString());
        this.closestToBallNode = new ClosestToBallNode(ally);
        this.chaseBallNode = new ChaseBallNode(ally);
        this.cutPassingLaneNode = new CutPassingLaneNode(ally);
        this.chasingBall = false;
    }

    @Override
    public NodeState execute() {
        // at desired frequency
        if (NodeState.isSuccess(this.closestToBallNode.execute()) != this.chasingBall) {
            switchBranch();
        }
        return NodeState.SUCCESS;
    }

    private void switchBranch() {
        if (this.chasingBall) {
            // stop chasing ball
            this.cutPassingLaneNode.execute(); // target of branchThread
        }
        else {
            // stop cutting passing lane
            this.chaseBallNode.execute(); // target of branch thread
        }
        this.chasingBall = !this.chasingBall;
    }

}
