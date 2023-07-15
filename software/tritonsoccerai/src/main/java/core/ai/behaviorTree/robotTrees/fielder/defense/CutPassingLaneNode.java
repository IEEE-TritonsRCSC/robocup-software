package main.java.core.ai.behaviorTree.robotTrees.fielder.defense;

import main.java.core.ai.GameInfo;
import main.java.core.ai.behaviorTree.nodes.NodeState;
import main.java.core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.ObjectHelper;
import main.java.core.ai.behaviorTree.robotTrees.basicFunctions.ChaseBallNode;

/**
 * Cuts passing lane between ball and a foe
 */
public class CutPassingLaneNode extends TaskNode {

    private final MoveToObjectNode moveToObjectNode;
    private final ChaseBallNode chaseBallNode;

    public CutPassingLaneNode(int allyID) {
        super("Cut Passing Lane Node: " + allyID, allyID);
        this.moveToObjectNode = new MoveToObjectNode(allyID);
        this.chaseBallNode = new ChaseBallNode(allyID);
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    @Override
    public NodeState execute() {
        // System.out.println("Running cut passing lane node");
        Robot foeToGuard = ObjectHelper.identifyFoeToGuard(GameInfo.getAlly(allyID), GameInfo.getFoeFielders());
        if (foeToGuard != null) {
            moveTowardFoe(foeToGuard);
        }
        else {
            chaseBallNode.execute();
        }
        return NodeState.SUCCESS;
    }

    /**
     * Moves ally toward a given foe
     */
    private void moveTowardFoe(Robot foe) {
        this.moveToObjectNode.execute(foe);
    }

}
