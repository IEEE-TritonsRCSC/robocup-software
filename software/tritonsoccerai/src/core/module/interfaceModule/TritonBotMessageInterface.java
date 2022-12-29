package core.module.interfaceModule;

import com.rabbitmq.client.Delivery;
import core.ai.GameInfo;
import core.constants.ProgramConstants;
import core.module.Module;
import com.triton.networking.UDP_Client;
import proto.triton.TritonBotCommunication.TritonBotMessage;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static core.messaging.Exchange.AI_ROBOT_FEEDBACKS;
import static core.messaging.Exchange.AI_TRITON_BOT_MESSAGE;
import static core.messaging.SimpleSerialize.simpleDeserialize;
import static proto.simulation.SslSimulationRobotFeedback.RobotFeedback;

public class TritonBotMessageInterface extends Module {
    private Map<Integer, UDP_Client> clientMap;
    private List<Future<?>> clientFutures;
    private Map<Integer, RobotFeedback> feedbacks;

    public TritonBotMessageInterface(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
        clientMap = new HashMap<>();
        clientFutures = new ArrayList<>();
        feedbacks = new HashMap<>();

        try {
            setupClients();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
        declareConsume(AI_TRITON_BOT_MESSAGE, this::callbackTritonBotMessage);
    }

    private void callbackTritonBotMessage(String s, Delivery delivery) {
        TritonBotMessage message = (TritonBotMessage) simpleDeserialize(delivery.getBody());
        if (clientMap.containsKey(message.getId()))
            clientMap.get(message.getId()).addSend(message.toByteArray());
    }

    @Override
    public void interrupt() {
        super.interrupt();
        clientFutures.forEach(clientFuture -> clientFuture.cancel(false));
    }

    private void setupClients() throws SocketException, UnknownHostException {
        for (int id = 0; id < ProgramConstants.gameConfig.numBots; id++) {
            String serverAddress;
            int serverPort;
            switch (GameInfo.getTeamColor()) {
                case YELLOW -> {
                    serverAddress = ProgramConstants.networkConfig.tritonBotAddressYellow;
                    serverPort = ProgramConstants.networkConfig.tritonBotPortBaseYellow + id * ProgramConstants.networkConfig.tritonBotPortIncr;
                }
                case BLUE -> {
                    serverAddress = ProgramConstants.networkConfig.tritonBotAddressBlue;
                    serverPort = ProgramConstants.networkConfig.tritonBotPortBaseBlue + id * ProgramConstants.networkConfig.tritonBotPortIncr;
                }
                default -> throw new IllegalStateException("Unexpected value: " + GameInfo.getTeamColor());
            }

            UDP_Client client = new UDP_Client(serverAddress, serverPort, this::callbackTritonBotFeedback, 10);
            clientMap.put(id, client);
        }
    }

    private void callbackTritonBotFeedback(byte[] bytes) {
        RobotFeedback feedback = null;
        try {
            feedback = RobotFeedback.parseFrom(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        feedbacks.put(feedback.getId(), feedback);
        publish(AI_ROBOT_FEEDBACKS, feedbacks);
    }

    @Override
    public void run() {
        super.run();
        for (int id = 0; id < ProgramConstants.gameConfig.numBots; id++) {
            RobotFeedback.Builder feedback = RobotFeedback.newBuilder();
            feedback.setId(id);
            feedback.setDribblerBallContact(false);
            feedbacks.put(id, feedback.build());
        }
        clientMap.forEach((id, client) -> clientFutures.add(executor.submit(client)));
    }
}
