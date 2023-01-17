package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.util.Vector2d;

//Task Nodes
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;

import core.fieldObjects.robot.Ally;

import static core.constants.ProgramConstants.objectConfig;

import java.lang.Math;

/**
 * Define whether to dribble or pass the ball
 */
public class MakePlayNode extends CompositeNode {
    private final DribbleBallNode dribble;
    private final CoordinatedPassNode coordinatedPass;

    public MakePlayNode(Ally ally) {
        super("Make Play");
        this.dribble = new DribbleBallNode(ally);
        this.coordinatedPass = new CoordinatedPassNode(ally);
    }
    
    @Override
    public NodeState execute() {

        //If there is a big space between robot and ball, dribble
        //otherwise, pass the ball to the other robots
       if(Math.sqrt((calcSpace().x*calcSpace().x)+(calcSpace().y*calcSpace().y)) > Float.MAX_VALUE) {
        this.dribble.execute();
       }
       else{
        this.coordinatedPass.execute();
       }

        return NodeState.SUCCESS;
    }

    /**
     * Calculate the sapce between the ball and the ball holder.
     * Also check if the robot is facing the ball.
     */
    private Vector2d calcSpace(){
        Vector2d offset; // offset 2Dvector between the robot and the ball

        // TODO Not sure how to check if the robot is facing the ball(Compute the facePos)

        if (facePos == null)
        // TODO Not sure how to get the robot orientation
            offset = new Vector2d((float) Math.cos(orientation), (float) Math.sin(orientation));
        else
            offset = facePos.sub(GameInfo.getAllyClosestToBall().getPose()).norm();

        //scale the offset
        offset = offset.scale(objectConfig.objectToCameraFactor * objectConfig.ballRadius
                + objectConfig.objectToCameraFactor * objectConfig.robotRadius);

        return offset;
    }

}
