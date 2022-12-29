package core.ai.behaviorTree.robotTrees.goalkeeper.offense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.CoordinatedPassNode;
import core.fieldObjects.robot.Ally;

public class PassBallNode extends TaskNode{

    private final CoordinatedPassNode passBall;

    public PassBallNode() {
        super("Pass Ball Node");
        // TODO 
        // Find optimal ally to pass to perhaps instead of closest ally 
        this.passBall = new CoordinatedPassNode(GameInfo.getAllyClosestToBall());
    }
    @Override
    public NodeState execute() {
        // TODO make the pass
        return this.passBall.execute();
    }

}
