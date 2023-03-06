package main.java.core.module.processingModule;

import com.rabbitmq.client.Delivery;
import main.java.core.module.Module;
import main.java.core.util.ConvertCoordinate;
import main.java.core.util.Vector2d;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_BIASED_SIMULATOR_CONTROL;
import static main.java.core.messaging.Exchange.AI_SIMULATOR_CONTROL;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslSimulationControl.*;


/*
 * Class for changing a robot biased simulator control to a audience biased simulator control
 */
public class SimulatorControlAudienceConverter extends Module {

    /**
     * Constructor for this class
     * @param executor executor to be used in parent constructor
     */
    public SimulatorControlAudienceConverter(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    /**
     * Meet requirement for object that extends module
     */
    @Override
    protected void prepare() {
    }

    /**
     * Consumes from AI_BIASED_ROBOT_COMMAND and runs callBackBiasedRobotCommand
     * @throws IOException
     * @throws TimeoutException
     */
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_BIASED_SIMULATOR_CONTROL, this::callbackBiasedSimulatorControl);
    }

    /**
     * Takes a message containing a biased robot command and publishes 
     * audience biased command to the AI_ROBOT_COMMAND exchange.
     * @param s - param not used
     * @param delivery - Message containing biased command for robot
     */
    private void callbackBiasedSimulatorControl(String s, Delivery delivery) {
        SimulatorControl biasedSimulatorControl = (SimulatorControl) simpleDeserialize(delivery.getBody());
        SimulatorControl simulatorControl = controlBiasedToAudience(biasedSimulatorControl);
        publish(AI_SIMULATOR_CONTROL, simulatorControl);
    }

    /**
     * Converts biased control to audience control
     * @param control The original control from the biased perspective
     * @return A control with the audience perspective
     */
    private static SimulatorControl controlBiasedToAudience(SimulatorControl control) {
        SimulatorControl.Builder audienceControl = control.toBuilder();
        audienceControl.setTeleportBall(teleportBallBiasedToAudience(control.getTeleportBall()));

        audienceControl.clearTeleportRobot();
        for (TeleportRobot teleportRobot : control.getTeleportRobotList()) {
            audienceControl.addTeleportRobot(teleportRobotBiasedToAudience(teleportRobot));
        }

        return audienceControl.build();
    }

    /**
     * Converts a ball from the biased perspective to audience perspective
     * @param teleportBall The ball from the biased perspective
     * @return The ball from the audience perspective
     */
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

    /**
     * Converts a robot from the biased perspective to audience perspective
     * @param teleportRobot The robot from the biased perspective
     * @return The robot from the audience perspective
     */
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
