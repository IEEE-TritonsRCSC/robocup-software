package core.ai.behaviorTree.robotTrees.fielder;

import core.ai.behaviorTree.BehaviorTree;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.ai.behaviorTree.nodes.conditionalNodes.ConditionalNode;

public class FielderTree extends BehaviorTree {

    public FielderTree() {
        super();
        this.root = new FielderRootNode();
    }

}
