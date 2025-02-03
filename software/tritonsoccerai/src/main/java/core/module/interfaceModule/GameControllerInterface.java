package core.module.interfaceModule;

import core.constants.ProgramConstants;
import core.module.Module;
import core.networking.UDP_MulticastReceiver;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import static core.messaging.Exchange.AI_GAME_CONTROLLER_WRAPPER;
import static proto.gc.SslGcRefereeMessage.Referee;

public class GameControllerInterface extends Module {
    Future<?> receiverFuture;
    private UDP_MulticastReceiver receiver;

    public GameControllerInterface(ScheduledThreadPoolExecutor executor) {
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
        receiver = new UDP_MulticastReceiver(ProgramConstants.networkConfig.gameControllerAddress,
                ProgramConstants.networkConfig.gameControllerDetectionPort,
                this::callbackWrapper);
    }

    /**
     * Called when a packet is received. Parses the packet and publishes it to exchanges.
     *
     * @param bytes the bytes of the packet received
     */
    private void callbackWrapper(byte[] bytes) {
        try {
            Referee wrapper = Referee.parseFrom(bytes);
            publish(AI_GAME_CONTROLLER_WRAPPER, wrapper);
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
