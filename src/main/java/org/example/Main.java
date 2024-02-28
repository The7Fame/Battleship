package org.example;

import org.example.file.FileGame;
import org.example.game.Game;
import org.example.socket.Client;
import org.example.socket.Server;

import java.io.IOException;
import java.util.*;

import static org.example.field.Constants.*;

public class Main {
    public static void main(String[] args) throws IOException {
        msgWelcome();
        System.out.println("Do you want to play on server with your enemy or on share PC with him?\n0 - server\n1 - share PC");
        Scanner in = new Scanner(System.in);
        FileGame gameDocumenting = new FileGame(nameOfFile());
        boolean flag = false;
        while (!flag) {
            String choice = in.nextLine();
            switch (choice) {
                case "0":
                    while (!flag){
                        System.out.println("Create a new server or connect to an existing one?\n0 - a new server\n1 - an existing server");
                        String res = in.nextLine();
                        switch (res){
                            case "0":
                                try {
                                    new Server().start();
                                    flag = true;
                                    break;
                                }catch (IOException e){
                                    System.out.println("Server is already bind");
                                    break;
                                }

                            case "1":
                                new Client().connect();
                                flag = true;
                                break;
                            default:
                                System.out.println("Invalid option. Please try again.");
                                break;
                        }
                    }
                    flag = true;
                    break;
                case "1":
                    Game game = new Game(in, gameDocumenting);
                    game.play();
                    flag = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

}