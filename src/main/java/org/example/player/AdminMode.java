package org.example.player;

import java.io.*;
import java.util.*;

import static org.example.field.Constants.clear;

public class AdminMode {

    private final File gamesFolder;
    private Scanner sc;
    private Set<String> games;

    public AdminMode(Scanner sc){
        this.sc = sc;
        this.gamesFolder = new File(System.getProperty("user.dir") + File.separator + "games");
        this.games = new HashSet<>();
    }
    private static void showMenu() {
        System.out.println("Welcome to the admin mode. Please choose an option:");
        System.out.println("1 - Number of games");
        System.out.println("2 - Read the game");
        System.out.println("3 - Delete the game");
        System.out.println("4 - Exit");
    }

    private void numberOfGames() {
        clear();
        int fileCount = gamesFolder.listFiles().length;
        System.out.println("Number of games: " + fileCount);
    }

    private void listOfGames() {
        File[] files = gamesFolder.listFiles();
        for (File file : files) {
            games.add(file.getName());
            System.out.println(file.getName());
        }
    }

    private void gameToRead(){
        clear();

        System.out.println("Enter the name of the file to read: ");
        listOfGames();
        String fileName = sc.nextLine();
        File file = new File(gamesFolder, fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("No such file");
        }
    }
    private void deleteGame() {
        clear();

        System.out.println("Enter the name of the file to delete: ");
        listOfGames();
        String fileName = sc.nextLine();
        File file = new File(gamesFolder, fileName);
        if (file.delete()) {
            System.out.println("File " + file.getName() + " deleted successfully");
        } else {
            System.out.println("File " + file.getName() + " could not be deleted");
        }

    }

    public void admin() {
        boolean exit = false;
        while (!exit) {
            showMenu();
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    numberOfGames();
                    break;
                case "2":
                    gameToRead();
                    break;
                case "3":
                    deleteGame();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Thank you for using the admin mode. Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
