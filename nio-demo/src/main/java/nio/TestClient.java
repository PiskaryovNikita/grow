package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestClient {
	private static final int CAPACITY = 8192;

	public static void startClient() {
		try (final SocketChannel socketChannel = SocketChannel
				.open(new InetSocketAddress("localhost", EchoNIOServer.PORT))) {

			final String threadName = Thread.currentThread().getName();
			final String[] messages = new String[]{threadName + ": msg1", threadName + ": msg2", threadName + ": " +
					"msg3", "Done"};

			for (final String message : messages) {
				final ByteBuffer request = ByteBuffer.allocate(CAPACITY);
				request.put(message.getBytes());
				request.flip();
				socketChannel.write(request);

				final ByteBuffer response = ByteBuffer.allocate(CAPACITY);
				socketChannel.read(response);
				response.flip();
				System.out.println(new String(response.array()).trim());
			}
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public static void main(final String[] args) {
		final Runnable runnable = TestClient::startClient;

		new Thread(runnable, "client-A").start();
		new Thread(runnable, "client-B").start();
	}

}
