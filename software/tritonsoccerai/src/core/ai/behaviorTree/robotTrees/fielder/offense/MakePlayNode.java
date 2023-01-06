package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.fieldObjects.robot.Ally;


/**
 * Define wheter to dribble or pass the ball 
 */
public class MakePlayNode extends CompositeNode {
    private final DribbleBallNode dribble;
    private final CoordinatedPassNode coordinatedPass;

    public MakePlayNode(Ally ally) {
        // TODO
        super("Make Play");
        this.dribble = new DribbleBallNode(ally);
        this.coordinatedPass = new CoordinatedPassNode(ally);
    }
    
    @Override
    public NodeState execute() {
        // TODO If there is a space between robot and ball, dribble
        // otherwise, pass the ball to the other robots
       if(hasSpace() == true) {
        this.dribble.execute();
       }
       else{
        this.coordinatedPass.execute();
       }

        return NodeState.SUCCESS;
    }

    //TODO Space in front of ball holder
    private boolean hasSpace(){
        boolean hasspace;

        hasspace = true;

        return hasspace;
    }

}
