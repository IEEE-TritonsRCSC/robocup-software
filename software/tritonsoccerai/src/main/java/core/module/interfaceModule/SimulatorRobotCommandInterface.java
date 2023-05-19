package main.java.core.module.interfaceModule;

import com.rabbitmq.client.Delivery;

import main.java.core.ai.GameInfo;
import main.java.core.constants.ProgramConstants;
import main.java.core.constants.Team;
import main.java.core.module.Module;
import main.java.core.networking.UDP_Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_ROBOT_COMMAND;
import static main.java.core.messaging.Exchange.AI_ROBOT_FEEDBACKS;
import static main.java.core.messaging.SimpleSerialize.simpleDeserialize;

import static proto.simulation.SslSimulationRobotControl.RobotCommand;
import static proto.simulation.SslSimulationRobotControl.RobotControl;
import static proto.simulation.SslSimulationRobotFeedback.RobotControlResponse;
import static proto.simulation.SslSimulationRobotFeedback.RobotFeedback;

public class SimulatorRobotCommandInterface extends Module {
    private UDP_Client client;

    private Map<Integer, RobotFeedback> feedbacks;

    private Future<?> clientFuture;

    public SimulatorRobotCommandInterface(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
        feedbacks = new HashMap<>();

        try {
            setupClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_ROBOT_COMMAND, this::callbackRobotCommand);
    }

    /**
     * Called when a message is received.
     * Creates the RobotCommand from a delivery and adds it to a sendQueue of a client.
     * 
     * @param s
     * @param delivery
     */
    private void callbackRobotCommand(String s, Delivery delivery) {
        // System.out.println("simRobComInt recieved command");
        RobotCommand robotCommand = (RobotCommand) simpleDeserialize(delivery.getBody());

        RobotControl.Builder robotControl = RobotControl.newBuilder();
        robotControl.addRobotCommands(robotCommand);

        client.addSend(robotControl.build().toByteArray());
    }

    @Override
    public void interrupt() {
        super.interrupt();
        clientFuture.cancel(false);
    }

    /**
     * Using simulationRobotControlAddress and simulationRobotControlPort, setup the udp client. 
     * 
     * @throws IOException
     */
    private void setupClient() throws IOException {
        String allyControlAddress;
        int allyControlPort;

        //Setup the ally control address and the ally control port
        switch (GameInfo.getTeamColor()) {
            case BLUE -> {
                allyControlAddress = ProgramConstants.networkConfig.simulationRobotControlAddressBlue;
                allyControlPort = ProgramConstants.networkConfig.simulationRobotControlPortBlue;
            }
            case YELLOW -> {
                allyControlAddress = ProgramConstants.networkConfig.simulationRobotControlAddressYellow;
                allyControlPort = ProgramConstants.networkConfig.simulationRobotControlPortYellow;
            }
            default -> throw new IllegalStateException("Unexpected value: " + GameInfo.getTeamColor());
        }

        //Setup a udp client
        client = new UDP_Client(allyControlAddress, allyControlPort, this::callbackRobotControlResponse, 10);
    }

    /**
     * Called when a response to RobotControl from the simulator to the connected client is received. 
     * Parses the response and publishes the robot feedback from the response.
     * 
     * @param bytes the bytes of the response received
     */
    private void callbackRobotControlResponse(byte[] bytes) {
        RobotControlResponse response = null;

        try {
            response = RobotControlResponse.parseFrom(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (RobotFeedback feedback : response.getFeedbackList())
            feedbacks.put(feedback.getId(), feedback);

        publish(AI_ROBOT_FEEDBACKS, feedbacks);
    }

    @Override
    public void run() {
        super.run();
        clientFuture = executor.submit(client); 
    }

}
