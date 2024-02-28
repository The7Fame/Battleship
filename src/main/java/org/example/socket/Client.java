package org.example.socket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public void connect() throws IOException{
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to the server");
        System.out.println("Put your name: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Thread reader = new Thread(new Runnable() {
            public void run() {
                try {
                    while (!socket.isClosed()){
                        String line = in.readLine();
                        if(line != null){
                            System.out.println(line);
                        }else {
                            throw new SocketException();
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        reader.start();

        Thread writer = new Thread(new Runnable() {
            public void run() {
                try {
                    Scanner scanner = new Scanner(System.in);
                    while (!socket.isClosed()){
                        String line = scanner.nextLine();
                        out.println(line);
                    }
                    scanner.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        writer.start();
    }
}
