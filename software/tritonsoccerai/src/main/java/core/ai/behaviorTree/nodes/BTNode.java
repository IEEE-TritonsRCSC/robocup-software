package main.java.core.ai.behaviorTree.nodes;

/**
 * Superclass for all types of nodes in a behavior tree
 * Implements java.lang.Runnable interface
 */
public abstract class BTNode implements Runnable {

    private final String name;

    public BTNode() {
        this.name = "BT Node";
    }

    public BTNode(String name) {
        this.name = name;
    }

    public abstract NodeState execute();

    public void stopExecution() {}

    /**
     * Default implementation of run() method,
     * used for running nodes in separate threads if needed
     */
    @Override
    public void run() {
        this.execute();
    }

    public String getName() {
        return this.name;
    }

}
