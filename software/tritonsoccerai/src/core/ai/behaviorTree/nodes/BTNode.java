package core.ai.behaviorTree.nodes;

public abstract class BTNode {

    private final String name;

    public BTNode() {
        this.name = "BT Node";
    }

    public BTNode(String name) {
        this.name = name;
    }

    public abstract NodeState execute();

    public String getName() {
        return this.name;
    }

}
