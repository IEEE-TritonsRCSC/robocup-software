package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.util.Vector2d;

//Task Nodes
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;

import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

import static core.constants.ProgramConstants.objectConfig;

import java.lang.Math;

/**
 * Define wheter to dribble or pass the ball 
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

        //If there is a space between robot and goal, dribble
        //otherwise, pass the ball to the other robots
       if(checkDribble()) {
        this.dribble.execute();
       }
       else{
        this.coordinatedPass.execute();
       }

        return NodeState.SUCCESS;
    }

    /**
     * Check if there is a space towards the goal
     */
    private boolean checkDribble(){
        boolean hasSpace = true;

        return hasSpace;
    }

}

