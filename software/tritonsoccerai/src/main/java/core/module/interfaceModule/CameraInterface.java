package main.java.core.module.interfaceModule;

import main.java.core.constants.ProgramConstants;
import main.java.core.module.Module;
import main.java.core.networking.UDP_MulticastReceiver;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static main.java.core.messaging.Exchange.AI_VISION_WRAPPER;

import static proto.vision.MessagesRobocupSslWrapper.SSL_WrapperPacket;

public class CameraInterface extends Module {
    Future<?> receiverFuture;
    private UDP_MulticastReceiver receiver;

    public CameraInterface(ScheduledThreadPoolExecutor executor) {
        super(executor);
    }

    @Override
    protected void prepare() {
        try {
            setupReceiver();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void declareConsumes() throws IOException, TimeoutException {
    }

    @Override
    public void interrupt() {
        super.interrupt();
        receiverFuture.cancel(false);
    }

    /**
     * Setup the udp multicast receiver
     *
     * @throws IOException
     */
    private void setupReceiver() throws IOException {
        // Setup a multicast receiver
        receiver = new UDP_MulticastReceiver(ProgramConstants.networkConfig.visionAddress,
                ProgramConstants.networkConfig.visionDetectionPort,
                this::callbackWrapper);
    }

    /**
     * Called when a packet is received. Parses the packet and publishes it to exchanges.
     *
     * @param bytes the bytes of the packet received
     */
    private void callbackWrapper(byte[] bytes) {
        try {
            SSL_WrapperPacket wrapper = SSL_WrapperPacket.parseFrom(bytes);
            publish(AI_VISION_WRAPPER, wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        receiverFuture = executor.submit(receiver);
    }

}
