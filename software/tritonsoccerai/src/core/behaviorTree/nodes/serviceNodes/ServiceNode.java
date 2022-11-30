package core.behaviorTree.nodes.serviceNodes;

import core.behaviorTree.nodes.BTNode;
import core.behaviorTree.nodes.NodeState;

// Attach to composite nodes and execute at defined frequency while branch is being executed;
// used to make checks and updates
public class ServiceNode extends BTNode {

    public ServiceNode() {
        super("Service Node");
    }

    public ServiceNode(String name) {
        super(name);
    }

    @Override
    public NodeState execute() {
        // TODO
        return NodeState.SUCCESS;
    }

}
