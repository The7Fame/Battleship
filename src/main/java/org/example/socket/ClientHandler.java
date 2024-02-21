package org.example.socket;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;


    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.name = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            name = (String) in.readObject();
            int participants = server.getParticipantCount();
            if (participants < 2) {
                sendMessage("Waiting for the connection another player...");
            }
            server.broadcast(name + " connected to the server");

            if (server.getParticipantCount() == 2) {
                server.broadcast("The game has started");
            }
            while (true) {
                String message = (String) in.readObject();
                if (message == null) {
                    break;
                }
                System.out.println(name + ": " + message);
                server.broadcast(name + ": " + message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            server.removeClient(this);
            server.broadcast(name + " left the server");
        }
    }
}

