package main.java.core.ai.behaviorTree.nodes;

/**
 * Defines every possible state that can be returned by a node
 */
public enum NodeState {
    RUNNING,
    SUCCESS,
    FAILURE;

    public static boolean isSuccess(NodeState state) {
        return state == SUCCESS;
    }

}
