package main.java.core.module.interfaceModule;

import main.java.core.constants.ProgramConstants;
import main.java.core.module.Module;
import main.java.core.networking.UDP_MulticastReceiver;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.List;

import static main.java.core.messaging.Exchange.AI_VISION_WRAPPER;

import static proto.vision.MessagesRobocupSslWrapper.SSL_WrapperPacket;
import static proto.vision.MessagesRobocupSslDetection.*;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionFrame;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionFrame.Builder;
import static proto.vision.MessagesRobocupSslDetection.SSL_DetectionRobot;

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
            if(wrapper.hasDetection()) {
                SSL_DetectionFrame frame = wrapper.getDetection();
                List<SSL_DetectionRobot> robots_yellow = frame.getRobotsYellowList();
                List<SSL_DetectionRobot> robots_blue = frame.getRobotsBlueList();
                for(SSL_DetectionRobot yrobot : robots_yellow) {
                    System.out.println("yellow robot" + yrobot.getRobotId() + ":" + yrobot.getX() + "," + yrobot.getY());
                }
                for(SSL_DetectionRobot brobot : robots_blue) {
                    System.out.println("blue robot" + brobot.getRobotId() + ":" + brobot.getX() + "," + brobot.getY());
                }
            }
            
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
