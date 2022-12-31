package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;


/**
 * Dribbles or passes the ball based on the location of foes
 */
public class MakePlayNode extends CompositeNode {
    private final DribbleBallNode dribble;
    private final CoordinatedPassNode coordinatedpass;

    public MakePlayNode(Ally ally) {
        // TODO
        super("Make Play");
        this.dribble = new DribbleBallNode(ally);
        this.coordinatedpass = new CoordinatedPassNode(ally);
    }
    
    @Override
    public NodeState execute() {
        //TODO
        //Determine if pass or dribble using available coordinate info
        this.dribble.execute();
        this.coordinatedpass.execute();

        return NodeState.SUCCESS;
    }

}
