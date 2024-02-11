package org.example.field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final int SIZE = 16;
    public static final int[] RAWS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    public static final char[] POSITION = {'v', 'h'};
    public static final char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p'};

    public static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static String nameOfFile(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm");
        String formattedDateTime = formatter.format(dateTime);
        return "game" + formattedDateTime + ".txt";
    }
}
