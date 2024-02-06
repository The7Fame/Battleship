package org.example;

import org.example.game.Game;
import org.example.ship.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = new Game(in);
        game.msgWelcome();
        game.play();
    }
}