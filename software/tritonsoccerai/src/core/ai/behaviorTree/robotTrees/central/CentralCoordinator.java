package core.ai.behaviorTree.robotTrees.central;

import core.ai.GameInfo;
import core.fieldObjects.robot.Ally;
import math.linearAlgebra.Vec2D;

// responsible for coordinating team actions
// ex. passing
public class CentralCoordinator {

    public static void execute() {
        // TODO
        // check for messages from robots
        // when there is a new message, call correct coordinating method
    }

    private static void coordinatedPass(Ally receiver, Vec2D passLoc) {
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
