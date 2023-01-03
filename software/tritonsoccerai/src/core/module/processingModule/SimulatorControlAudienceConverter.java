package com.triton.module.processing_module;

import com.rabbitmq.client.Delivery;
import com.triton.module.Module;
import com.triton.util.ConvertCoordinate;
import com.triton.util.Vector2d;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static com.triton.messaging.Exchange.AI_BIASED_SIMULATOR_CONTROL;
import static com.triton.messaging.Exchange.AI_SIMULATOR_CONTROL;
import static com.triton.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslSimulationControl.*;

public class SimulatorControlAudienceConverter extends Module {
    public SimulatorControlAudienceConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_BIASED_SIMULATOR_CONTROL, this::callbackBiasedSimulatorControl);
    }

    private void callbackBiasedSimulatorControl(String s, Delivery delivery) {
        SimulatorControl biasedSimulatorControl = (SimulatorControl) simpleDeserialize(delivery.getBody());
        SimulatorControl simulatorControl = controlBiasedToAudience(biasedSimulatorControl);
        publish(AI_SIMULATOR_CONTROL, simulatorControl);
    }

    private static SimulatorControl controlBiasedToAudience(SimulatorControl control) {
        SimulatorControl.Builder audienceControl = control.toBuilder();
        audienceControl.setTeleportBall(teleportBallBiasedToAudience(control.getTeleportBall()));

        audienceControl.clearTeleportRobot();
        for (TeleportRobot teleportRobot : control.getTeleportRobotList()) {
            audienceControl.addTeleportRobot(teleportRobotBiasedToAudience(teleportRobot));
        }

        return audienceControl.build();
    }

    private static TeleportBall teleportBallBiasedToAudience(TeleportBall teleportBall) {
        TeleportBall.Builder audienceTeleportBall = teleportBall.toBuilder();

        Vector2d audiencePosition = ConvertCoordinate.biasedToAudience(teleportBall.getX(), teleportBall.getY());
        audienceTeleportBall.setX(audiencePosition.x);
        audienceTeleportBall.setY(audiencePosition.y);

        Vector2d audienceVelocity = ConvertCoordinate.biasedToAudience(teleportBall.getVx(), teleportBall.getVy());
        audienceTeleportBall.setVx(audienceVelocity.x);
        audienceTeleportBall.setVy(audienceVelocity.y);

        return audienceTeleportBall.build();
    }

    private static TeleportRobot teleportRobotBiasedToAudience(TeleportRobot teleportRobot) {
        TeleportRobot.Builder audienceTeleportRobot = teleportRobot.toBuilder();

        Vector2d audiencePosition = ConvertCoordinate.biasedToAudience(teleportRobot.getX(), teleportRobot.getY());
        audienceTeleportRobot.setX(audiencePosition.x);
        audienceTeleportRobot.setY(audiencePosition.y);

        Vector2d audienceVelocity = ConvertCoordinate.biasedToAudience(teleportRobot.getVX(), teleportRobot.getVY());
        audienceTeleportRobot.setVX(audienceVelocity.x);
        audienceTeleportRobot.setVY(audienceVelocity.y);

        audienceTeleportRobot.setOrientation(ConvertCoordinate.biasedToAudience(teleportRobot.getOrientation()));
        audienceTeleportRobot.setVAngular(ConvertCoordinate.biasedToAudience(teleportRobot.getVAngular()));

        return audienceTeleportRobot.build();
    }
}
