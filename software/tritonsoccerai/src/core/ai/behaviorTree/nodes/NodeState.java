package core.ai.behaviorTree.nodes;

public enum NodeState {
    RUNNING,
    SUCCESS,
    FAILURE;

    public static boolean isSuccess(NodeState state) {
        if (state == SUCCESS) {
            return true;
        }
        else {
            return false;
        }
    }

}
