package behaviorTree.nodes;

public class BTNode {

    private final String name;

    public BTNode() {
        this.name = "BT Node";
    }

    public BTNode(String name) {
        this.name = name;
    }

    public boolean execute() {
        // overwrite this method
        return true;
    }

}
