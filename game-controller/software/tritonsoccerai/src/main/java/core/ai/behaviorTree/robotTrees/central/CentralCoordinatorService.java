package main.java.core.ai.behaviorTree.robotTrees.central;

import main.java.core.ai.behaviorTree.nodes.serviceNodes.ServiceNode;
import main.java.core.ai.GameInfo;
import main.java.core.ai.GameState;
import static proto.triton.FilteredObject.Robot;
import main.java.core.util.Vector2d;
import main.java.core.ai.behaviorTree.nodes.NodeState;

import static proto.gc.SslGcRefereeMessage.Referee;

import static main.java.core.util.ProtobufUtils.getPos;

// TODO : Not sure how to send pass details and process them
public class CentralCoordinatorService extends ServiceNode {

    // used to track starting position of ball
    // to determine if ball kicked into play
    private Vector2d ballStartPos = null;

    public CentralCoordinatorService() {
        super("Central Coordinator Service");
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
    public NodeState execute() {
        float DISTANCE_CONSTANT = (float) 0.2;
        if (GameInfo.getCurrCommand() == Referee.Command.NORMAL_START) {
            /*if (this.ballStartPos == null) {
                this.ballStartPos = getPos(GameInfo.getBall());
            }
            // check if ball kicked
            if (getPos(GameInfo.getBall()).dist(ballStartPos) > DISTANCE_CONSTANT) {
                // if so, switch current game state to OPEN_PLAY
                GameInfo.setCurrState(GameState.OPEN_PLAY);
            }*/
            GameInfo.setInOpenPlay(true);
        }
        // System.out.println(String.valueOf(GameInfo.getBall().getX()) + " " + String.valueOf(GameInfo.getBall().getY()));
        // else check for new message to act upon
        return NodeState.SUCCESS;
    }
    
}
