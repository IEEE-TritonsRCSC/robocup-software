package core.behaviorTree.robotTrees;

import core.behaviorTree.BehaviorTree;
import core.behaviorTree.nodes.NodeState;
import core.behaviorTree.nodes.compositeNodes.CompositeNode;
import core.fieldObjects.robot.Ally;

import java.util.ArrayList;

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
