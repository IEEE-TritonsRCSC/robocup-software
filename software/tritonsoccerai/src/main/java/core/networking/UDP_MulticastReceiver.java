package main.java.core.networking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;

public class UDP_MulticastReceiver extends Thread {
    private static final String NETWORK_INTERFACE = "bge0";
    private static final int PACKET_BUFFER_SIZE = 9999;

    private final MulticastSocket socket;
    private final byte[] buf = new byte[PACKET_BUFFER_SIZE];
    private final Consumer<byte[]> callbackPacket;

    public UDP_MulticastReceiver(String multicastAddressName, int mutlicastPort, Consumer<byte[]> callbackPacket) throws IOException {
        this.callbackPacket = callbackPacket;

        socket = new MulticastSocket(mutlicastPort);
        InetAddress multicastAddress = InetAddress.getByName(multicastAddressName);
        InetSocketAddress group = new InetSocketAddress(multicastAddress, mutlicastPort);
        NetworkInterface networkInterface = NetworkInterface.getByName(NETWORK_INTERFACE);
        socket.joinGroup(group, networkInterface);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData(),
                        packet.getOffset(),
                        packet.getLength());
                byte[] bytes = stream.readAllBytes();
                stream.close();
                callbackPacket.accept(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}