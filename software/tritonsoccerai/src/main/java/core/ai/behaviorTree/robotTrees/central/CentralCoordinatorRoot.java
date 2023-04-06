package main.java.core.ai.behaviorTree.robotTrees.central;

import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;

import static main.java.core.util.ProtobufUtils.getPos;

public class CentralCoordinatorRoot implements Runnable {

    private final CentralCoordinatorService centralRootService;

    // used to track starting position of ball
    // to determine if ball kicked into play
    private Vector2d ballStartPos = null;

    public CentralCoordinatorRoot(){
        super("Central Coordinator Root");

        this.centralRootService = new CentralCoordinatorService();

    }

    /**
     * Sends appropriate messages to robot trees based on provided pass details
     * Instructs receiver to expect ball at a given pass location
     * Instructs all other robot trees to position based on pass location
     */
    private static void coordinatedPass(Robot receiver, Vector2d passLoc) {
        // TODO
        for (Robot ally : GameInfo.getAllies()) {
            if (ally == receiver) {
                // send message to receiver to expect ball at passLoc
            }
            else {
                // send message to all other robots to position based on passLoc
            }
        }
    }

    /**
     * Check for messages from robots and check if current state is NormalStart
     * When new message received, call correct coordinating method
     * If current state is NormalStart, change current state when ball kicked
     */
    @Override
    public void run() {
        float DISTANCE_CONSTANT = (float) 0.2;
        if (GameInfo.getCurrState() == GameState.NORMAL_START) {
            if (this.ballStartPos == null) {
                this.ballStartPos = getPos(GameInfo.getBall());
            }
            // check if ball kicked
            if (getPos(GameInfo.getBall()).dist(ballStartPos) > DISTANCE_CONSTANT) {
                // if so, switch current game state to OPEN_PLAY
                GameInfo.setCurrState(GameState.OPEN_PLAY);
            }
        }
        // else check for new message to act upon
    }

}
