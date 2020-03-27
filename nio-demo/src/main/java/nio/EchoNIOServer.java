package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Advantage of Nio server is that one thread can observe many clients.
 * Applicable only when socket isn't closed after response is sent(http).
 * Example: chat, responsive-ui, web-games
 * <p>
 * Non blocking: (Socket example)
 * 1. read() doesn't block if some bytes not available(e.g. network issues), it reads as many bytes as available.
 * 2. write() doesn't wait for client to accept all bytes, it writes as many as client can accept.
 * <p>
 * Netty server is build on NIO
 */
public class EchoNIOServer {
    public final static int PORT = 8081;
    public static final String HOST = "localhost";
    private static final String CLOSE_CONNECTION_SIGNAL = "Done";

    private Selector selector;

    public static void main(final String[] args) {
        try {
            new EchoNIOServer().startServer();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        selector = Selector.open();
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(HOST, PORT));
        System.out.println("Server started on port " + PORT);

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            final int channels = selector.select();
            if (channels == 0) {
                continue;
            }

            final Set<SelectionKey> readyKeys = selector.selectedKeys();
            final Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                final SelectionKey key = iterator.next();

                // Remove key from set so we don't process it twice
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    handleAcceptKey(key);
                } else if (key.isReadable()) {
                    handleReadKey(key);
                } else {
                    throw new IllegalArgumentException("Cannot handle key");
                }
            }
        }
    }

    /**
     * Register selector for channel for further IO (record it for read/write
     * operations, here we have used read operation)
     */
    private void handleAcceptKey(final SelectionKey key) throws IOException {
        final ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        final SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        System.out.println("Connected to: " + channel.socket().getRemoteSocketAddress());

        final int operations = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        channel.register(selector, operations);
    }

    /**
     * Communicate with multiple clients and close socket only if client request to
     */
    private static void handleReadKey(final SelectionKey key) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(256);
        final SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        client.configureBlocking(false);
        if (new String(buffer.array()).trim().equals(CLOSE_CONNECTION_SIGNAL)) {
            final SocketAddress remoteAddress = client.getRemoteAddress();
            client.close();
            System.out.printf("Not accepting messages from client %s%n", remoteAddress);
            return;
        }

        buffer.flip();

        System.out.println(new String(buffer.array()));
        client.write(buffer);
    }
}
