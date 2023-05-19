package main.java.core.module.processingModule;

import com.rabbitmq.client.Delivery;
import main.java.core.module.Module;
import main.java.core.util.ConvertCoordinate;
import main.java.core.util.Vector2d;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static main.java.core.messaging.Exchange.AI_ROBOT_COMMAND;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslSimulationRobotControl.*;

/**
 *  Class for changing a robot biased command to a audience biased command
 */
public class RobotCommandAudienceConverter extends Module {
    
    /*
     * Constructor
     */
    public RobotCommandAudienceConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
    }

    /*
     * Consumes from AI_BIASED_ROBOT_COMMAND and runs callBackBiasedRobotCommand
     */
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_BIASED_ROBOT_COMMAND, this::callbackBiasedRobotCommand);
    }

    /**
     * Takes a message containing a biased robot command and publishes 
     * audience biased command to the AI_ROBOT_COMMAND exchange.
     * @param s - param not used
     * @param delivery - Message containing biased command for robot
     */
    private void callbackBiasedRobotCommand(String s, Delivery delivery) {
        RobotCommand biasedRobotCommand = (RobotCommand) simpleDeserialize(delivery.getBody());
        RobotCommand robotCommand = commandBiasedToAudience(biasedRobotCommand);
        // System.out.println("RobComInt published to AI_ROBOT_COMMAND");
        publish(AI_ROBOT_COMMAND, robotCommand);
    }

    /**
     * Takes a robot biased command and builds audience biased command
     * @param command - robot biased command 
     * @return - audience biased command
     */
    private static RobotCommand commandBiasedToAudience(RobotCommand command) {
        RobotCommand.Builder audienceCommand = command.toBuilder();
        audienceCommand.setMoveCommand(moveCommandBiasedToAudience(command.getMoveCommand()));
        return audienceCommand.build();
    }

    /**
     * Takes a robot biased move command and builds audience biased move command
     * @param moveCommand - robot biased move command
     * @return - audience biased move command
     */
    private static RobotMoveCommand moveCommandBiasedToAudience(RobotMoveCommand moveCommand) {
        if (moveCommand.hasGlobalVelocity()) {
            RobotMoveCommand.Builder audienceMoveCommand = moveCommand.toBuilder();
            audienceMoveCommand.setGlobalVelocity(moveGlobalVelocityBiasedToAudience(audienceMoveCommand.getGlobalVelocity()));
            return audienceMoveCommand.build();
        }

        return moveCommand;
    }

    /**
     * Takes in a robot biased movement velocity and returns audience biased movement velocity
     * @param globalVelocity - robot biased movement velocity 
     * @return - audience biased movement velocity 
     */
    private static MoveGlobalVelocity moveGlobalVelocityBiasedToAudience(MoveGlobalVelocity globalVelocity) {
        MoveGlobalVelocity.Builder audienceGlobalVelocity = globalVelocity.toBuilder();

        Vector2d audiencePosition = ConvertCoordinate.biasedToAudience(globalVelocity.getX(), globalVelocity.getY());
        audienceGlobalVelocity.setX(audiencePosition.x);
        audienceGlobalVelocity.setY(audiencePosition.y);

        return audienceGlobalVelocity.build();
    }
}
