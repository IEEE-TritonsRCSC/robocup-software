package core.ai;

import core.ai.behaviorTree.robotTrees.FielderTree;
import core.ai.behaviorTree.robotTrees.GoalkeeperTree;

public class AI {

    private FielderTree fielderTree;
    private GoalkeeperTree goalkeeperTree;

    public AI() {
        // TODO
        this.fielderTree = new FielderTree(); // edit to pass in a list of fielders
        this.goalkeeperTree = new GoalkeeperTree(); // edit to pass in the goalkeeper
        fielderTree.execute();
        goalkeeperTree.execute();
    }

}
