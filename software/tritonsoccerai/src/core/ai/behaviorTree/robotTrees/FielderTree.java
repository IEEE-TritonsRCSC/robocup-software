package core.ai.behaviorTree.robotTrees;

import core.ai.behaviorTree.BehaviorTree;
import core.ai.behaviorTree.nodes.NodeState;
import core.ai.behaviorTree.nodes.compositeNodes.CompositeNode;

public class FielderTree extends BehaviorTree {

    public FielderTree() {
        super();
        initialize();
    }

    public void initialize() {
        // TODO
        this.root = new CompositeNode("Fielder Root") {
            @Override
            public NodeState execute() {
                // TODO
                return NodeState.SUCCESS;
            }
        };

    }

}
