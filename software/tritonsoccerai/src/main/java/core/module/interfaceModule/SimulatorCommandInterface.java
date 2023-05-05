package main.java.core.module.interfaceModule;

import com.google.protobuf.Any;
import com.rabbitmq.client.Delivery;

import main.java.core.constants.ProgramConstants;
import main.java.core.constants.Team;
import main.java.core.module.Module;
import main.java.core.networking.UDP_Client;

import proto.simulation.SslSimulationConfig;
import proto.simulation.SslSimulationConfig.SimulatorConfig;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_SIMULATOR_CONFIG;
import static main.java.core.messaging.Exchange.AI_SIMULATOR_CONTROL;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslGcCommon.RobotId;
import static proto.simulation.SslGcCommon.Team.BLUE;
import static proto.simulation.SslGcCommon.Team.YELLOW;
import static proto.simulation.SslSimulationControl.*;
import static sslsim.SslSimulationCustomErforceRobotSpec.RobotSpecErForce;

public class SimulatorCommandInterface extends Module {
    private UDP_Client client;
    private Future<?> clientFuture;

    public SimulatorCommandInterface(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
        try {
            setupClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_SIMULATOR_CONTROL, this::callbackSimulatorControl);
        declareConsume(AI_SIMULATOR_CONFIG, this::callbackSimulatorConfig);
    }

    /**
     * Called when a message is received.
     * Creates the simulator_control from a delivery and adds it to a sendQueue of a client.
     * 
     * @param s
     * @param delivery
     */
    private void callbackSimulatorControl(String s, Delivery delivery) {
        SimulatorControl simulatorControl = (SimulatorControl) simpleDeserialize(delivery.getBody());

        SimulatorCommand.Builder simulatorCommand = SimulatorCommand.newBuilder();
        simulatorCommand.setControl(simulatorControl);
        client.addSend(simulatorCommand.build().toByteArray());
    }

    /**
     * Called when a message is received.
     * Creates the simulator_config from a delivery and adds it to a sendQueue of a client.
     * 
     * @param s
     * @param delivery
     */  
    private void callbackSimulatorConfig(String s, Delivery delivery) {
        SimulatorConfig simulatorConfig = (SimulatorConfig) simpleDeserialize(delivery.getBody());

        SimulatorCommand.Builder simulatorCommand = SimulatorCommand.newBuilder();
        simulatorCommand.setConfig(simulatorConfig);
        client.addSend(simulatorCommand.build().toByteArray());
    }

    @Override
    public void interrupt() {
        super.interrupt();
        clientFuture.cancel(false);
    }

    /**
     * Setup the udp client
     * 
     * @throws IOException
     */
    private void setupClient() throws IOException {
        //Setup a udp client
        client = new UDP_Client(ProgramConstants.networkConfig.simulationCommandAddress,
                ProgramConstants.networkConfig.simulationCommandPort,
                this::callbackSimulatorResponse,
                10);
    }

    /**
     * Called when a response of the simulator to the connected client is received. 
     * Parses the response and displays it.
     *
     * @param bytes the bytes of the response received
     */
    private void callbackSimulatorResponse(byte[] bytes) {
        try {
            SimulatorResponse simulatorResponse = SimulatorResponse.parseFrom(bytes);
            System.out.println("Sim response:");
            System.out.println(simulatorResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        setupSimulator();
        clientFuture = executor.submit(client);
    }

    /**
     * Setup the robots in the simulator
     * 
     */    
    private void setupSimulator() {
        SimulatorControl.Builder simulatorControl = SimulatorControl.newBuilder();

        //Setup blue team robots
        for (int id = 6; id < 12; id++) {
            TeleportRobot.Builder teleportRobot = TeleportRobot.newBuilder();
            RobotId.Builder robotId = RobotId.newBuilder();
            robotId.setTeam(BLUE);
            robotId.setId(id);
            teleportRobot.setId(robotId);
            teleportRobot.setPresent(false);
            simulatorControl.addTeleportRobot(teleportRobot);
        }

        //Setup yellow team robots
        for (int id = 6; id < 12; id++) {
            TeleportRobot.Builder teleportRobot = TeleportRobot.newBuilder();
            RobotId.Builder robotId = RobotId.newBuilder();
            robotId.setTeam(YELLOW);
            robotId.setId(id);
            teleportRobot.setId(robotId);
            teleportRobot.setPresent(false);
            simulatorControl.addTeleportRobot(teleportRobot);
        }

        System.out.println("SimCommandInt published to AI_SIMULATOR_CONTROL");
        publish(AI_SIMULATOR_CONTROL, simulatorControl.build());

        SimulatorConfig.Builder simulatorConfig = SimulatorConfig.newBuilder();
        for (int id = 0; id < 6; id++) {
            SslSimulationConfig.RobotSpecs.Builder specs = SslSimulationConfig.RobotSpecs.newBuilder();
            addSpecs(Team.YELLOW, id, specs);
            addSpecs(Team.BLUE, id, specs);
            simulatorConfig.addRobotSpecs(specs);
        }

        System.out.println("SimCommandInt published to AI_SIMULATOR_CONFIG");
        publish(AI_SIMULATOR_CONFIG, simulatorConfig.build());
    }

    /**
     * Setup the specs of the robots in the simulator
     * 
     * @param team
     * @param id the robot id
     * @param specs 
     */
    private void addSpecs(Team team, int id, SslSimulationConfig.RobotSpecs.Builder specs) {
        RobotId.Builder robotId = RobotId.newBuilder();
        if (team == Team.YELLOW)
            robotId.setTeam(YELLOW);
        else
            robotId.setTeam(BLUE);
        robotId.setId(id);

        //Setup the robot specs
        specs.setId(robotId);
        specs.setRadius(ProgramConstants.objectConfig.robotRadius);
        specs.setHeight(ProgramConstants.objectConfig.robotHeight);
        specs.setMass(ProgramConstants.objectConfig.robotMass);
        specs.setMaxLinearKickSpeed(ProgramConstants.objectConfig.robotMaxLinearKickSpeed);
        specs.setMaxChipKickSpeed(ProgramConstants.objectConfig.robotMaxChipKickSpeed);
        specs.setCenterToDribbler(ProgramConstants.objectConfig.robotCenterToDribbler);

        SslSimulationConfig.RobotLimits.Builder limits = SslSimulationConfig.RobotLimits.newBuilder();
        limits.setAccSpeedupAbsoluteMax(ProgramConstants.objectConfig.robotAccSpeedupAbsoluteMax);
        limits.setAccSpeedupAngularMax(ProgramConstants.objectConfig.robotAccSpeedupAngularMax);
        limits.setAccBrakeAbsoluteMax(ProgramConstants.objectConfig.robotAccBrakeAbsoluteMax);
        limits.setAccBrakeAngularMax(ProgramConstants.objectConfig.robotAccBrakeAngularMax);
        limits.setVelAbsoluteMax(ProgramConstants.objectConfig.robotVelAbsoluteMax);
        limits.setVelAngularMax(ProgramConstants.objectConfig.robotVelAngularMax);
        specs.setLimits(limits);

        SslSimulationConfig.RobotWheelAngles.Builder wheelAngles = SslSimulationConfig.RobotWheelAngles.newBuilder();
        wheelAngles.setFrontRight(ProgramConstants.objectConfig.robotFrontRight);
        wheelAngles.setBackRight(ProgramConstants.objectConfig.robotBackRight);
        wheelAngles.setBackLeft(ProgramConstants.objectConfig.robotBackLeft);
        wheelAngles.setFrontLeft(ProgramConstants.objectConfig.robotFrontLeft);
        specs.setWheelAngles(wheelAngles);

        RobotSpecErForce.Builder specErForce = RobotSpecErForce.newBuilder();
        specErForce.setShootRadius(ProgramConstants.objectConfig.shootRadius);
        specErForce.setDribblerHeight(ProgramConstants.objectConfig.dribblerHeight);
        specErForce.setDribblerWidth(ProgramConstants.objectConfig.dribblerWidth);
        specs.addCustom(Any.pack(specErForce.build()));
    }
}
