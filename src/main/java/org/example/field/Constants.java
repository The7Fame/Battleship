package org.example.field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final int SIZE = 16;
    public static final char[] POSITION = {'v', 'h'};
    public static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void msgWelcome() {
        System.out.println(
                """
                Welcome to Battleship!
                Coordinates input example (a1 h 5)
                a1 - coordinate
                h - position
                5 - size of the ship
                """
        );
    }

    public static String nameOfFile(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm");
        String formattedDateTime = formatter.format(dateTime);
        return "game" + formattedDateTime + ".txt";
    }
}
