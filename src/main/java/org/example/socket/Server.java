package org.example.socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static final int PORT = 8000;
    private List<ClientHandler> clients;
    private ServerSocket serverSocket;
    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on " + PORT);
            clients = new ArrayList<>();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("The player is connected: " + socket);
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getClientCount() {
        return clients.size();
    }

    public int getParticipantCount() {
        return getClientCount();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}
