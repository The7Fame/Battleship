package org.example;

import org.example.file.FileGame;
import org.example.game.Game;

import java.io.IOException;
import java.util.*;

import static org.example.field.Constants.nameOfFile;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        FileGame gameDocumenting = new FileGame(nameOfFile());
        Game game = new Game(in, gameDocumenting);
        game.msgWelcome();
        game.play();
    }
}