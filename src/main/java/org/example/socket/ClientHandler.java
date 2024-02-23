package org.example.socket;

import org.example.file.FileGame;
import org.example.game.Game;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static org.example.field.Constants.nameOfFile;

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
            server.setActiveClient(this);

            if (server.getParticipantCount() == 2) {
                server.broadcast("The game has started");
            }
            while (true) {
                String message = (String) in.readObject();
                if (message == null) {
                    break;
                }
                if (isActive()) {
                    System.out.println(name + ": " + message);
                    server.broadcast(name + ": " + message);
                    startGame();
                    switchActiveClient();
                } else {
                    sendMessage("not your turn");
                }
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

    public boolean isActive() {
        return this == server.getActiveClient();
    }

    public void switchActiveClient() {
        int index = server.clients.indexOf(this);
        index = (index + 1) % server.clients.size();
        server.setActiveClient(server.clients.get(index));
    }

    public void startGame() throws IOException {
        FileGame gameDocumenting = new FileGame(nameOfFile());
        new Game(new Scanner(System.in), gameDocumenting).play();
    }
}

