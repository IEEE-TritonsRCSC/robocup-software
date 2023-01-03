package com.triton.module.processing_module;

import com.rabbitmq.client.Delivery;
import com.triton.module.Module;
import com.triton.util.ConvertCoordinate;
import com.triton.util.Vector2d;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static com.triton.messaging.Exchange.AI_BIASED_ROBOT_COMMAND;
import static com.triton.messaging.Exchange.AI_ROBOT_COMMAND;
import static com.triton.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslSimulationRobotControl.*;

public class RobotCommandAudienceConverter extends Module {

    public RobotCommandAudienceConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_BIASED_ROBOT_COMMAND, this::callbackBiasedRobotCommand);
    }

    private void callbackBiasedRobotCommand(String s, Delivery delivery) {
        RobotCommand biasedRobotCommand = (RobotCommand) simpleDeserialize(delivery.getBody());
        RobotCommand robotCommand = commandBiasedToAudience(biasedRobotCommand);
        publish(AI_ROBOT_COMMAND, robotCommand);
    }

    private static RobotCommand commandBiasedToAudience(RobotCommand command) {
        RobotCommand.Builder audienceCommand = command.toBuilder();
        audienceCommand.setMoveCommand(moveCommandBiasedToAudience(command.getMoveCommand()));
        return audienceCommand.build();
    }

    private static RobotMoveCommand moveCommandBiasedToAudience(RobotMoveCommand moveCommand) {
        if (moveCommand.hasGlobalVelocity()) {
            RobotMoveCommand.Builder audienceMoveCommand = moveCommand.toBuilder();
            audienceMoveCommand.setGlobalVelocity(moveGlobalVelocityBiasedToAudience(audienceMoveCommand.getGlobalVelocity()));
            return audienceMoveCommand.build();
        }

        return moveCommand;
    }

    private static MoveGlobalVelocity moveGlobalVelocityBiasedToAudience(MoveGlobalVelocity globalVelocity) {
        MoveGlobalVelocity.Builder audienceGlobalVelocity = globalVelocity.toBuilder();

        Vector2d audiencePosition = ConvertCoordinate.biasedToAudience(globalVelocity.getX(), globalVelocity.getY());
        audienceGlobalVelocity.setX(audiencePosition.x);
        audienceGlobalVelocity.setY(audiencePosition.y);

        return audienceGlobalVelocity.build();
    }
}
