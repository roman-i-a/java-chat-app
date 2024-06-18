package com.rom.aviloff.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
    
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 2000;

    private Socket socket = null;
    private BufferedReader inputConsole = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public ChatClient(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to server: " + hostname);

            inputConsole = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = "";
            while (!line.equals("exit")) {
                line = inputConsole.readLine();
                out.println(line);
                String inputString = in.readLine();
                System.out.println(inputString);
            }

            socket.close();
            inputConsole.close();
            out.close();
        } catch (UnknownHostException unknownHostException) {
            System.out.println("Host unknown: " + unknownHostException.getMessage());
        } catch (IOException exception) {
            System.out.println("Unknown exception: " + exception.getMessage());
        }

    }
    


    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("127.0.0.1", 5000);
    }
}
