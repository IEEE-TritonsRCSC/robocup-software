package core.ai.behaviorTree.robotTrees.central;

import core.ai.GameInfo;
import core.fieldObjects.robot.Ally;
import core.util.Vector2d;

/**
 * Responsible for coordinating team actions such as passing
 */
public class CentralCoordinator {

    /**
     * Check for messages from robots at defined frequency
     * When new message received, call correct coordinating method
     */
    public static void execute() {
        // TODO
    }

    /**
     * Sends appropriate messages to robot trees based on provided pass details
     * Instructs receiver to expect ball at a given pass location
     * Instructs all other robot trees to position based on pass location
     */
    private static void coordinatedPass(Ally receiver, Vector2d passLoc) {
        // TODO
        for (Ally ally : GameInfo.getAllies()) {
            if (ally == receiver) {
                // send message to receiver to expect ball at passLoc
            }
            else {
                // send message to all other robots to position based on passLoc
            }
        }
    }

}
