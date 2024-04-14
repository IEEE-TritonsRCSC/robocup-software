package main.java.core.module.aiModule;

import com.rabbitmq.client.Delivery; // Class that encapsulates a message

import main.java.core.module.Module;
import main.java.core.ai.GameInfo;

import static proto.gc.SslGcRefereeMessage.Referee;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

// enum for all Exchange relevant Objects
import static main.java.core.messaging.Exchange.*;
 // method for deserializing the message (in bytes)
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;

/**
 * AI Module - refer to Figure 1
 * Houses all game-related info
 */
public class AIModule extends Module {

    public AIModule(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
    }

    /**
     * Consumes from AI_GAME_CONTROLLER_WRAPPER and runs callbackWrapper
     */
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_GAME_CONTROLLER_WRAPPER, this::callbackWrapper);
        declareConsume(CENTRAL_COORDINATOR_PASSING, this::passingCallBack);
    }

    /**
     * Takes a message containing an audience biased wrapper and adds appropriate info to GameInfo.
     * @param s - param not used
     * @param delivery - Message containing audience biased wrapper
     */
    private void callbackWrapper(String s, Delivery delivery) {
        Referee wrapper = (Referee) simpleDeserialize(delivery.getBody());
        if ((wrapper.getCommand() == Referee.Command.NORMAL_START) && (GameInfo.getCurrCommand() != Referee.Command.NORMAL_START)) {
            GameInfo.setPrevCommand(GameInfo.getCurrCommand());
        }
        if (wrapper.getCommand() == Referee.Command.FORCE_START) {
            GameInfo.setInOpenPlay(true);
        }
        else if (wrapper.getCommand() != Referee.Command.NORMAL_START) {
            GameInfo.setInOpenPlay(false);
        }
        if ((GameInfo.getReferee() != null) && (wrapper.getCommand() != GameInfo.getCurrCommand())) {
            System.out.println(wrapper.getCommand());
        }
        GameInfo.setReferee(wrapper);
    }

    private void passingCallBack(String s, Delivery delivery) {
        CoordinatedPass pass = (CoordinatedPass) simpleDeserialize(delivery.getBody());
        GameInfo.setCoordinatedPass(pass);
    }

    @Override
    public void run() {
        super.run();
    }

}
