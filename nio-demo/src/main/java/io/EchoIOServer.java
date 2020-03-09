package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoIOServer {
	public static final int PORT = 8080;

	public static void main(String[] args) throws IOException {
		new EchoIOServer().startServer();
	}

	public void startServer() throws IOException {
		System.out.println("Waiting on port : " + PORT + "...");
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(() -> {
					try {
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						String request, response;
						while ((request = in.readLine()) != null) {
							response = processRequest(request);
							out.println("Server echo: " + response);
							if ("Done".equals(request)) {
								break;
							}
						}
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}).start();
			}
		}
	}

	private static String processRequest(String request) {
		System.out.println("Request: " + request);
		return request;
	}
}
