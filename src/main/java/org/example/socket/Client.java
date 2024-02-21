package org.example.socket;

import java.io.*;
import java.net.*;

public class Client {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8000;
    private Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;
    public Client(String name) {
        this.name = name;

        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Connected to the server: " + socket);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(name);
            out.flush();
            String message = (String) in.readObject();
            System.out.println(message);

            new Thread(new ServerHandler()).start();

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                message = keyboard.readLine();
                out.writeObject(message);
                out.flush();
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
        }
    }

    private class ServerHandler implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) in.readObject();
                    System.out.println(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}