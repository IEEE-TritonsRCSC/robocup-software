package core.ai;

import core.ai.behaviorTree.robotTrees.fielder.FielderTree;
import core.ai.behaviorTree.robotTrees.goalkeeper.GoalkeeperTree;
import core.fieldObjects.robot.Ally;

import java.util.ArrayList;

public class AI {

    private ArrayList<FielderTree> fielderTrees;
    private GoalkeeperTree goalkeeperTree;

    public AI() {
        // TODO
        // initialize GameInfo
        // create channels for each ally
        this.fielderTrees = new ArrayList<FielderTree>();
        for (Ally fielder : GameInfo.getFielders()) {
            fielderTrees.add(new FielderTree(fielder));
        }
        this.goalkeeperTree = new GoalkeeperTree();
        for (FielderTree tree : this.fielderTrees) {
            tree.execute();
        }
        goalkeeperTree.execute();
    }

}
