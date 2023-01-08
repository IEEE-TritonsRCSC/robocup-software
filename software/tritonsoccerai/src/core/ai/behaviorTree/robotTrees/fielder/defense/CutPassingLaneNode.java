package core.ai.behaviorTree.robotTrees.fielder.defense;

import core.ai.GameInfo;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.taskNodes.TaskNode;
import core.ai.behaviorTree.robotTrees.basicFunctions.MoveToObjectNode;
import core.fieldObjects.robot.Ally;
import core.fieldObjects.robot.Foe;

import java.util.ArrayList;

/**
 * Cuts passing lane between ball and a foe
 */
public class CutPassingLaneNode extends TaskNode {

        private final MoveToObjectNode moveToObjectNode;

    public CutPassingLaneNode(Ally ally) {
        super("Cut Passing Lane Node: " + ally.toString(), ally);
        this.moveToObjectNode = new MoveToObjectNode(ally);
    }

    /**
     * Identifies a foe to guard and moves ally towards that foe
     */
    @Override
    public NodeState execute() {
        moveTowardFoe(identifyFoeToGuard());
        return NodeState.SUCCESS;
    }

    /**
     * Identifies a foe for ally to guard based on positions of all allies and foes
     * Refer to figure 8 for detailed explanation
     */
    private Foe identifyFoeToGuard() {
        // make copy of GameInfo.getFoeFielders() and sort it
        ArrayList<Foe> foesInOrderOfDistance = new ArrayList<>(GameInfo.getFoeFielders());
        foesInOrderOfDistance.sort((o1, o2) -> (int) (o1.getPos().dist(ally.getPos()) - o2.getPos().dist(ally.getPos())));
        // make copy of GameInfo.getFielders() and remove the ally closest to ball
        ArrayList<Ally> allyGuarders = new ArrayList<>(GameInfo.getFielders());
        allyGuarders.remove(GameInfo.getAllyClosestToBall());
        // For each foe, find n if ally is the nth farthest allyGuarder from foe
        // place sum will be n + foe's index in foesInOrderOfDistance
        int minPlaceSum = Integer.MAX_VALUE;
        int minPlaceSumIndex = 0;
        for (int i = 0; i < foesInOrderOfDistance.size(); i++) {
            Foe foe = foesInOrderOfDistance.get(i);
            float distToFoe = foe.getPos().dist(this.ally.getPos());
            int place = 0;
            for (Ally ally : allyGuarders) {
                if (foe.getPos().dist(ally.getPos()) <= distToFoe) {
                    place++;
                }
            }
            if ((i + place) < minPlaceSum) {
                minPlaceSum = i + place;
                minPlaceSumIndex = i;
            }
        }
        // return the foe with the lowest place sum
        return foesInOrderOfDistance.get(minPlaceSumIndex);
    }

    /**
     * Moves ally toward a given foe
     */
    private void moveTowardFoe(Foe foe) {
        this.moveToObjectNode.execute(foe);
    }

}
