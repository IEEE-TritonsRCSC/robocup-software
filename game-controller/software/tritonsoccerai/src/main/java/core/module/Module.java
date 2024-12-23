package main.java.core.module;

import com.rabbitmq.client.*;
import main.java.core.constants.ProgramConstants;
import main.java.core.messaging.Exchange;
import main.java.core.ai.GameInfo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.SimpleSerialize.simpleSerialize;

public abstract class Module extends Thread {

    private static final String CONNECTION_FACTORY_HOST = "localhost";
    private static final String FANOUT = "fanout";

    public ScheduledThreadPoolExecutor executor;

    private ConnectionFactory factory;
    private Channel publish_channel;
    private Channel consume_channel;

    public Module(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
        try {
            setupChannel();
            prepare();
            declareConsumes();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void setupChannel() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(CONNECTION_FACTORY_HOST);

        Connection publish_connection = factory.newConnection();
        publish_channel = publish_connection.createChannel();

        Connection consume_connection = factory.newConnection();
        consume_channel = consume_connection.createChannel();
    }

    protected abstract void prepare();

    protected abstract void declareConsumes() throws IOException, TimeoutException;

    /**
     * Declares an exchange to consume from. The messageConsumer function will be called when an message is received
     * from the exchange.
     *
     * @param exchange the exchange to consume from
     * @param callback the function to call once a message is received
     * @throws IOException
     */
    public void declareConsume(Exchange exchange, DeliverCallback callback) throws IOException, TimeoutException {
        consume_channel.exchangeDeclare(exchange.name() + GameInfo.getTeamColor().name(), FANOUT);

        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 1000);
        args.put("x-expires", 1000);
        String queueName = consume_channel.queueDeclare("",
                false,
                false,
                false,
                args).getQueue();
        consume_channel.queueBind(queueName, exchange.name() + GameInfo.getTeamColor().name(), "");
        consume_channel.queuePurge(queueName);

        DeliverCallback wrappedCallback = (s, delivery) -> {
            try {
//                long timeDiff = new Date().getTime() - delivery.getProperties().getTimestamp().getTime();
//                if (timeDiff > 10000) {
//                    System.out.println("RabbitMQ Bottleneck Warning: " + timeDiff + " ms");
//                    System.out.println("Class: " + this.getClass());
//                    System.out.println("Exchange: " + exchange.name());
//                } else {
                callback.handle(s, delivery);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        consume_channel.basicConsume(queueName, true, wrappedCallback, consumerTag -> {
        });
    }

    /**
     * Publishes to an exchange
     *
     * @param exchange the exchange to publish to
     * @param object   the object to send
     * @throws IOException
     */
    public void publish(Exchange exchange, Object object) {
        Date date = new Date();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().timestamp(date).build();
        if (publish_channel.isOpen()) {
            try {
                declarePublish(exchange);
                publish_channel.basicPublish(exchange.name() + GameInfo.getTeamColor().name(), "", properties, simpleSerialize(object));
            } catch (IOException | AlreadyClosedException ignored) {
            }
        }
    }

    /**
     * Declares an exchange to publish to
     *
     * @param exchange the exchange
     * @throws IOException
     */
    public void declarePublish(Exchange exchange) throws IOException {
        publish_channel.exchangeDeclare(exchange.name() + GameInfo.getTeamColor().name(), FANOUT);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            consume_channel.close();
            publish_channel.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
