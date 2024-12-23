package core.ai.behaviorTree.robotTrees.goalkeeper.defense;

import java.lang.Math;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToPositionNode;
import static proto.triton.FilteredObject.Robot;
import proto.vision.MessagesRobocupSslGeometry.SSL_GeometryFieldSize;
import core.util.Vector2d;

import static core.util.ProtobufUtils.getPos;

/**
 * Moves goalkeeper to optimal position to block ball
 */
public class BlockBallNode extends TaskNode {

    private final Vector2d centerArc;
    private final float radius;
    private final MoveToPositionNode moveToPositionNode;
    private boolean onLine;

    public BlockBallNode(int allyID) {
        super(allyID);
        this.centerArc = new Vector2d(0, -1 * (GameInfo.getField().getFieldLength() / 2)); // arbitrary center arc
        this.radius = GameInfo.getField().getGoalWidth() / 2.0f;
        this.moveToPositionNode = new MoveToPositionNode(allyID);
        this.onLine = false;
    }

    /**
     * Gets optimal position and moves robot to that location
     */
    @Override
    public NodeState execute() {
        // System.out.println("running block ball node");
        Vector2d positioningLocation;
        if (this.onLine) {
            positioningLocation = findLinePositioningLocation();
        }
        else {
            positioningLocation = findArcPositioningLocation();
        }
        this.moveToPositionNode.execute(positioningLocation);
        return NodeState.SUCCESS;
    }

    /**
     * Finds optimal location to position self
     */
    public Vector2d findArcPositioningLocation() {
        // finds optimal vector on the arc
        Vector2d optimalLocation;
        // Robot closestFoe = GameInfo.getFoeClosestToBall();
        // if (GameInfo.getFoePossessBall(closestFoe.getId()) && facingGoal(closestFoe)) {
        //     // TODO - move to best location on arc based on orientation of closestFoe
        //     float theta = closestFoe.getOrientation();
        //     float l = GameInfo.getField().getFieldLength() / 2.0f;
        //     float xB = getPos(GameInfo.getBall()).x;
        //     float yB = getPos(GameInfo.getBall()).x;
        //     float b = 2.0f * (xB * (float) Math.cos(theta) + ((l + yB) * (float) Math.sin(theta)));
        //     float c = (float) Math.pow(xB, 2) + (float) Math.pow(l + yB, 2) - this.radius;
        //     float H = ((-1.0f * b) + (float) Math.sqrt(Math.pow(b, 2) - (4 * c))) / 2.0f;
        //     return new Vector2d(xB - (H * (float) Math.cos(theta)), 
        //                         yB - (H * (float) Math.sin(theta)));
        // }
        // else {
            float mag = centerArc.dist(new Vector2d(getPos(GameInfo.getBall())));
            optimalLocation = new Vector2d(
                    this.centerArc.x + (((GameInfo.getBall().getX() - this.centerArc.x)/mag) * radius),
                    this.centerArc.y + (((GameInfo.getBall().getY() - this.centerArc.y)/mag) * radius));
        // }
        return optimalLocation; 
    }

    public Vector2d findLinePositioningLocation() {
        float ballX = getPos(GameInfo.getBall()).x;
        if (ballX < 0) {
            return new Vector2d(Math.max(ballX, GameInfo.getField().getGoalWidth() / -2.1f), GameInfo.getField().getFieldLength() / -2.0f);
        }
        else {
            return new Vector2d(Math.min(ballX, GameInfo.getField().getGoalWidth() / 2.1f), GameInfo.getField().getFieldLength() / -2.0f);
        }
    }

    private boolean facingGoal(Robot foe) {
        return Math.abs(goalX(getPos(foe), new Vector2d((float) Math.cos(foe.getOrientation()), (float) Math.sin(foe.getOrientation())))) 
                        < (GameInfo.getField().getGoalWidth() / 1.9f);
    }

    private float goalX(Vector2d start, Vector2d direction) {
        return start.x + (direction.x * ((GameInfo.getField().getFieldLength() - start.y) / direction.y));
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

}
