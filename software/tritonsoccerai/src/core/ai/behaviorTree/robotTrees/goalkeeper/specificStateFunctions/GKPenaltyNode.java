package core.ai.behaviorTree.robotTrees.goalkeeper.specificStateFunctions;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.SequenceNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.offense.GKPositionSelfNode;
import core.fieldObjects.robot.Ally;

import static core.util.ObjectHelper.distToPath;
import static proto.triton.FilteredObject.FilteredWrapperPacket;
import static proto.triton.FilteredObject.Robot;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

public class GKPenaltyNode extends SequenceNode {

    private final Ally ally;
    private final BlockBallNode blockBallNode;
    private final MoveToPositionNode moveToPositionNode;
    private FilteredWrapperPacket wrapper;


    //Penalty: GK has to be on the goal line, cant be on arc. Optimal pos: middle of the goal keeper line 
    public GKPenaltyNode(Ally ally, FilteredWrapperPacket wrapper) {
        super("Prepare Penalty Node : " + ally.toString());
        this.ally = ally;
        this.blockBallNode = new BlockBallNode(ally);
        this.moveToPositionNode = new MoveToPositionNode(ally);
        this.wrapper = wrapper;
    }

    @Override
    public NodeState execute() {
        if(!GameInfo.getPossessBall()) {
            this.blockBallNode.execute();
        }
        return NodeState.SUCCESS;
    }

}
