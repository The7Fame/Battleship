package org.example;

import org.example.file.FileGame;
import org.example.game.Game;
import org.example.socket.Client;
import org.example.socket.Server;

import java.io.IOException;
import java.util.*;

import static org.example.field.Constants.msgWelcome;
import static org.example.field.Constants.nameOfFile;

public class Main {
    public static void main(String[] args) throws IOException {
        msgWelcome();
        System.out.println("Do you want to play on server with your enemy or on share PC with him?\n0 - server\n1 - share PC");
        Scanner in = new Scanner(System.in);
        FileGame gameDocumenting = new FileGame(nameOfFile());
        String choice = in.nextLine();
        if(choice.equals("0")){
            System.out.println("Create a new server or connect to an existing one?\n0 - a new server\n1 - an existing server");
            String res = in.nextLine();
            if(res.equals("0")){
                new Server().start();}
            else if(res.equals("1")) {
                new Client().connect();
            }
        }else if (choice.equals("1")){
            Game game = new Game(in, gameDocumenting);
            game.play();
        }
    }
}