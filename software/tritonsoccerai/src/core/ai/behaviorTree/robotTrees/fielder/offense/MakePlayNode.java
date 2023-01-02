package core.ai.behaviorTree.robotTrees.fielder.offense;

import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.DribbleBallNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.fieldObjects.robot.Ally;


/**
 * Dribbles or passes the ball based on the location of foes
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
        // TODO
        // Determine if pass or dribble using available coordinate info
        this.dribble.execute();
        this.coordinatedPass.execute();

        return NodeState.SUCCESS;
    }

}
