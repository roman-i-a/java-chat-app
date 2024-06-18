package com.rom.aviloff.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatServer {

    private static List<ClientHandler> clients = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        int port = 5000;
        if (args.length != 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Illegal port. Try valid port.");
                System.exit(-1);
            }
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Server started on port [%d]. Waiting for clients...\n", port);
            
            while (true) {
                Socket clienSocket = serverSocket.accept();
                System.out.println("Client connected: " + clienSocket);
                
                ClientHandler clientThread = new ClientHandler(clienSocket, clients);
                clients.add(clientThread);
                new Thread(clientThread).start();
            }            
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
}


class ClientHandler implements Runnable {

    private List<ClientHandler> clients;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                for (ClientHandler c : clients) {
                    c.out.println(inputLine);
                }
            }
        } catch (IOException exception) {
            System.out.println("An error ocurred: " + exception.getLocalizedMessage());
        } finally {
            try {
                clientSocket.close();
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
