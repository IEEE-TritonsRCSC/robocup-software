package core.ai.behaviorTree.robotTrees.goalkeeper.defense.defenseRoot;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import core.ai.behaviorTree.robotTrees.goalkeeper.defense.BlockBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;

import static core.util.ProtobufUtils.getPos;
import static proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;

/**
 * Defined behavior for goalkeeper when on defense
 * Always blocks ball
 */
public class GKDefenseRootService extends ServiceNode {

    private final BlockBallNode blockBall;
    private final ChaseBallNode chaseBall;

    private final float WIDTH = 4000f;
    private final float DEPTH = 3000f;

    public GKDefenseRootService() {
        super("GK Defense Root Service");
        this.blockBall = new BlockBallNode(0);
        this.chaseBall = new ChaseBallNode(0);
    }

    /**
     * Always block ball
     */
    @Override
    public NodeState execute() {
        SSL_GeometryFieldSize field = GameInfo.getField();
        // System.out.println(GameInfo.getAllyClosestToBallIncGK().getId());
        if ((GameInfo.getAllyClosestToBallIncGK().getId() == 0) 
            && (((getPos(GameInfo.getFoeClosestToBall())).dist(getPos(GameInfo.getBall())) 
                - getPos(GameInfo.getKeeper()).dist(getPos(GameInfo.getBall()))) > 400)
            && getPos(GameInfo.getBall()).isInRect(WIDTH / -2.0f,
                                                    -0.5f * field.getFieldLength(),
                                                    WIDTH, DEPTH)) {
            this.chaseBall.execute();
        }
        else {
            this.blockBall.execute();
        }
        return NodeState.SUCCESS;
    }

}
