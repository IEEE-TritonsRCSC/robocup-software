package main.java.core.networking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class UDP_MulticastClient extends Thread {
    private static final String NETWORK_INTERFACE = "bge0";
    private static final int BUF_SIZE = 9999;
    private static final int QUEUE_CAPACITY = 5;

    public final int serverPort;
    private final InetAddress serverAddress;
    private final Consumer<byte[]> callbackPacket;

    private final MulticastSocket socket;
    private final BlockingQueue<byte[]> sendQueue;

    public UDP_MulticastClient(String serverAddress, int serverPort, Consumer<byte[]> callbackPacket) throws IOException {
        this(serverAddress, serverPort, callbackPacket, 100);
    
}

public UDP_MulticastClient(String serverAddress, int serverPort, Consumer<byte[]> callbackPacket, int timeout) throws IOException {
    super();
    this.serverAddress = InetAddress.getByName(serverAddress);
    this.serverPort = serverPort;
    this.callbackPacket = callbackPacket;

    socket = new MulticastSocket();
    socket.setReuseAddress(true);

    InetAddress multicastAddress = InetAddress.getByName(serverAddress);
    InetSocketAddress group = new InetSocketAddress(multicastAddress, serverPort);
    NetworkInterface networkInterface = NetworkInterface.getByName(NETWORK_INTERFACE);

    socket.joinGroup(group, networkInterface);
    socket.setSoTimeout(timeout);
    sendQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
}

@Override
public void run() {
    while (!isInterrupted())
        receive(send());
}

private void receive(boolean receive) {
    if (!receive || callbackPacket == null) return;

    byte[] buf = new byte[BUF_SIZE];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try {
        socket.receive(packet);
    } catch (IOException e) {
        return;
    }

    try {
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

private boolean send() {
    try {
        byte[] bytes = sendQueue.take();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, serverAddress, serverPort);
        socket.send(packet);
    } catch (InterruptedException | IOException e) {
        e.printStackTrace();
    }
    return true;
}

public void addSend(byte[] bytes) {
    if (sendQueue.remainingCapacity() == 0)
        sendQueue.poll();
    sendQueue.add(bytes);
}
}
