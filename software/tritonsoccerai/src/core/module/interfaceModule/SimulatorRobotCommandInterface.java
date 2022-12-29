package core.module.interfaceModule;

import com.rabbitmq.client.Delivery;
import core.ai.GameInfo;
import core.constants.ProgramConstants;
import core.module.Module;
import com.triton.networking.UDP_Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static core.messaging.Exchange.AI_ROBOT_COMMAND;
import static core.messaging.Exchange.AI_ROBOT_FEEDBACKS;
import static core.messaging.SimpleSerialize.simpleDeserialize;
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

    private void callbackRobotCommand(String s, Delivery delivery) {
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

    private void setupClient() throws IOException {
        String allyControlAddress;
        int allyControlPort;

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

        client = new UDP_Client(allyControlAddress, allyControlPort, this::callbackRobotControlResponse, 10);
    }

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
