package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ClosestToBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;
import core.fieldObjects.robot.Ally;

public class GKKickoffNode extends SequenceNode {

    private final Ally ally;
    private final GKPositionSelfNode positionSelfNode;
    private final BlockBallNode blockBallNode;

    public GKKickoffNode(Ally ally) {
        super("Prepare Kickoff Node : " + ally.toString());
        this.ally = ally;
        this.positionSelfNode = new GKPositionSelfNode(ally, null);
        this.blockBallNode = new BlockBallNode(ally);
    }

    @Override
    public NodeState execute() {
        if (GameInfo.getPossessBall()) {
            this.positionSelfNode.execute(); // set up GK on offense
        } else {
            while(true) {
                this.blockBallNode.execute(); // set up GK on defense
            }    
        }
        return NodeState.SUCCESS;
    }

}
