package java.core.ai.behaviorTree.nodes.serviceNodes;

import java.core.ai.behaviorTree.nodes.BTNode;
import java.core.ai.behaviorTree.nodes.NodeState;

/**
 * Attach to composite nodes and execute at defined frequency while branch is being executed
 * Used to make checks and updates
 */
public abstract class ServiceNode extends BTNode {

    public ServiceNode() {
        super("Service Node");
    }

    public ServiceNode(String name) {
        super(name);
    }

}
