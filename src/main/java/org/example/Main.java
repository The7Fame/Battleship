package org.example;

import org.example.game.Game;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = new Game(in);
        game.msgWelcome();
        game.play();
    }
}