package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        Runnable runnable = () -> new TestClient().startClient();
        new Thread(runnable, "client-A").start();
        new Thread(runnable, "client-B").start();
    }

    public void startClient() {
        String[] messages = new String[]{"msg1", "msg2", "msg3", "Done"};

        try {
            Socket socket = new Socket("localhost", EchoIOServer.PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            for (final String message : messages) {
                out.println(String.format("%s %s",Thread.currentThread().getName(), message));
                System.out.println(in.readLine());
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
