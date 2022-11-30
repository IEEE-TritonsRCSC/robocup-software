package behaviorTree.nodes;

public abstract class BTNode {

    private final String name;
    protected static final String RUNNING = "running";
    protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";

    public BTNode() {
        this.name = "BT Node";
    }

    public BTNode(String name) {
        this.name = name;
    }

    public String execute() {
        // overwrite this method
        return SUCCESS;
    }

    public String getName() {
        return this.name;
    }

}
