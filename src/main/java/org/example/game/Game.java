package org.example.game;

import org.example.field.Field;
import org.example.field.State;
import org.example.file.FileGame;
import org.example.player.AdminMode;
import org.example.player.Player;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.example.field.Constants.*;

public class Game implements Runnable{
    private final Scanner input;
    private Player player;
    private Player enemy;
    private int turn;
    private List<Player> players;
    private final FileGame gameFile;

    public Game(List<Player> players){
        this.players = players;
        this.input = new Scanner(System.in);
        this.gameFile = new FileGame(nameOfFile());
    }
    public Game(Scanner input, FileGame gameFile) {
        this.turn = 0;
        this.input = input;
        this.gameFile = gameFile;
    }

    public void play() throws IOException {
        createPlayers();
        if(player.getName().equalsIgnoreCase("admin")){
            new AdminMode(this.input).admin();
        }else  {
            Field playerField = new Field();
            Field enemyField = new Field();
            System.out.println(player.getName() + ", your turn to set up ships");
            player.setField(placeShips(playerField, player.isBot()));
            System.out.println(enemy.getName() + ", your turn to set up ships");
            enemy.setField(placeShips(enemyField, enemy.isBot()));
            gameFile.gameStart();
            while (playerField.getLivePoints() > 0 && enemyField.getLivePoints() > 0) {
                clear();
                if (turn == 0) {
                    System.out.println(player.getName() + ", your turn to attack");
                    gameFile.writeMessage(player.getName().toUpperCase() + " attack enemy field: ");
                    attack(playerField, enemyField, player.isBot());
                } else {
                    System.out.println(enemy.getName() + ", your turn to attack");
                    gameFile.writeMessage(enemy.getName().toUpperCase() + " attack enemy field: ");
                    attack(enemyField, playerField, enemy.isBot());
                }
            }
            winner();
        }
    }

    public void run(){
        try
        {
            broadcast("The game has started");
            gameFile.writeMessage("The game has started\n");
            while (players.get(0).getField().getLivePoints() > 0 && players.get(1).getField().getLivePoints() > 0)
            {
                Player player = players.get(turn);
                sendField(player);
                broadcast("It's player "+ player.getName() + "'s turn");
                player.getOut().println("Enter the coordinates");
                String input = player.getIn().readLine();
                if(isValidCoords(input)){
                    switch (player.getEnemy().getField().attack(parseCoords(input))) {
                        case HIT -> {
                            clear();
                            broadcast("Stricked");
                            gameFile.writeMessage("Stricked\n");
                        }
                        case DEAD -> {
                            clear();
                            broadcast("Destroyed");
                            gameFile.writeMessage("Destroyed\n");
                        }
                        default -> {
                            clear();
                            broadcast("Miss");
                            sendField(player);
                            turn = (turn + 1) % 2;
                            gameFile.writeMessage("Miss\n");
                        }
                    }
                }else {
                    clear();
                    broadcast("Wrong coordinates");
                    sendField(player);
                    turn = (turn + 1) % 2;
                    gameFile.writeMessage("Wrong coordinates\n");
                }
            }
            if (players.get(0).getField().getLivePoints() <= 0){
                broadcast(players.get(1).getName() + " has won");
                gameFile.gameEnd(players.get(1).getName());
                gameFile.writeMessage("     Your field     " + " ".repeat(20) + "     Enemy field     " + "\n");
                for (int i = 0; i < SIZE + 1; i++) {
                    gameFile.writeMessage(players.get(1).getField().drawField(false)[i] + " ".repeat(5) + players.get(1).getEnemy().getField().drawField(false)[i]+ "\n");
                }
            }else {
                broadcast(players.get(0).getName() + " has won");
                gameFile.gameEnd(players.get(0).getName());
                gameFile.writeMessage("     Your field     " + " ".repeat(20) + "     Enemy field     " + "\n");
                for (int i = 0; i < SIZE + 1; i++) {
                    gameFile.writeMessage(players.get(0).getField().drawField(false)[i] + " ".repeat(5) + players.get(0).getEnemy().getField().drawField(false)[i]+ "\n");
                }
            }
            for(Player player : players){
                player.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void broadcast(String message){
        for(Player player : players){
            player.getOut().println(message);
        }
    }

    public void sendField(Player player){
        player.getOut().printf("%-60s", "        Your field");
        player.getOut().printf("%-60s\n", "        Enemy field");
        for (int i = 0; i < SIZE + 1; i++) {
            player.getOut().printf("%-60s%-60s%n", player.getField().drawField(false)[i], player.getEnemy().getField().drawField(true)[i]);
        }
    }

    private void createPlayers() {
        System.out.print("Name of the first player: ");
        String name = this.input.nextLine();
        if(name.equalsIgnoreCase("admin")){
            this.player = new Player(name);
        }else{
            this.player = new Player(name);
            System.out.println("With whom do you want to play\n0 - human\n1 - computer");
            String choice = this.input.nextLine();
            switch (choice) {
                case "0":
                    System.out.print("Name of the second player: ");
                    this.enemy = new Player(this.input.nextLine());
                    break;
                case "1":
                    this.enemy = new Player();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter either 0 or 1.");
                    createPlayers();
            }
        }
    }

    private void winner() throws IOException {
        gameFile.writeMessage("     Your field     " + " ".repeat(20) + "     Enemy field     " + "\n");
        System.out.printf("%-60s", "        Your field");
        System.out.printf("%-60s\n", "        Enemy field");
        if (this.player.getField().getLivePoints() <= 0) {
            for (int i = 0; i < SIZE + 1; i++) {
                gameFile.writeMessage(this.enemy.getField().drawField(false)[i] + " ".repeat(5) + this.player.getField().drawField(false)[i]+ "\n");
                System.out.printf("%-60s%-60s%n", this.enemy.getField().drawField(false)[i], this.player.getField().drawField(false)[i]);
            }
            gameFile.gameEnd(this.enemy.getName());
            System.out.print(this.enemy.getName() + ", your are the winner");
        } else {
            for (int i = 0; i < SIZE + 1; i++) {
                gameFile.writeMessage(this.player.getField().drawField(false)[i] + " ".repeat(5) + this.enemy.getField().drawField(false)[i] + "\n");
                System.out.printf("%-60s%-60s%n", this.player.getField().drawField(false)[i], this.enemy.getField().drawField(false)[i]);
            }
            gameFile.gameEnd(this.player.getName());
            System.out.print(this.player.getName() + ", your are the winner");
        }
    }

    private void attack(Field myField, Field enemyField, boolean bot) throws IOException {
        if(!bot) {
            System.out.printf("%-60s", "        Your field");
            System.out.printf("%-60s\n", "        Enemy field");
            for (int i = 0; i < SIZE + 1; i++) {
                System.out.printf("%-60s%-60s%n", myField.drawField(false)[i], enemyField.drawField(true)[i]);
            }
        }
        switch (attackShips(enemyField, bot)) {
            case HIT -> {
                clear();
                System.out.println("Stricked");
                gameFile.writeMessage("Stricked\n");
            }
            case DEAD -> {
                clear();
                System.out.println("Destroyed");
                gameFile.writeMessage("Destroyed\n");
            }
            default -> {
                clear();
                System.out.println("Miss");
                this.turn = (this.turn + 1) % 2;
                gameFile.writeMessage("Miss\n");
            }
        }
    }

    private State attackShips(Field field, boolean bot) {
        System.out.print("Coordinates for the attack: ");
        while (true) {
            try {
                return field.attack(bot ? randomCoordsToAttack() : getCoords());
            } catch (Exception e) {
                System.out.print("Wrong coordinates ");
            }
        }
    }

    private Field placeShips(Field field, boolean bot) {
        clear();
        if (!bot){
            for (int i = 0; i < SIZE + 1; i++) {
                System.out.println(field.drawField(false)[i]);
            }
            System.out.println("How do your want do set up ships?\n0 - manually\n1 - automatically");
            String choice = this.input.nextLine();
            if(choice.equals("0")){
                while (field.getLivePoints() != 56) {
                    System.out.print("Put a ship on your board ");
                    addShips(field);
                    for (int i = 0; i < SIZE + 1; i++) {
                        System.out.println(field.drawField(false)[i]);
                    }
                }
                System.out.println(field.getLivePoints());
            } else if (choice.equals("1")) {
                while (field.getLivePoints() != 56){
                    field.setShipsAutomatically();
                }

            }
            return field;
        }
        while (field.getLivePoints() != 56){
            field.setShipsAutomatically();
        }
        return field;
    }

    private String[] parseCoords(String coords) throws IOException {
        String[] c = coords.split(" ");
        String[] coordinates = null;
        if(c.length == 3) {
            coordinates = new String[Integer.parseInt(c[2])];
            ArrayList<String> chars = new ArrayList<>();
            if (c[1].equalsIgnoreCase("h")) {
                for (
                        char ch = c[0].charAt(0);
                        ch < (char) (c[0].charAt(0) + Integer.parseInt(c[2]));
                        ch++
                ) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ch).append(c[0].substring(1));
                    chars.add(sb.toString());
                }
                for (int i = 0; i < Integer.parseInt(c[2]); i++) {
                    coordinates[i] = chars.get(i);
                }
            } else if (c[1].equalsIgnoreCase("v")) {
                for (
                        int i = Integer.parseInt(c[0].substring(1));
                        i < Integer.parseInt(c[0].substring(1)) + Integer.parseInt(c[2]);
                        i++
                ) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(c[0].charAt(0)).append(i);
                    chars.add(sb.toString());
                }
                for (int i = 0; i < Integer.parseInt(c[2]); i++) {
                    coordinates[i] = chars.get(i);
                }
            }
        } else if (c.length == 1) {
            gameFile.writeMessage(coords + " - ");
            coordinates = new String[1];
            for (int i = 0; i < 1; i++) {
                coordinates[i] = c[i];
            }
        }
        return coordinates;
    }

    private String[] getCoords() throws IOException {
        String coords = this.input.nextLine();
        return parseCoords(coords);
    }

    private String[] randomCoordsToAttack() throws IOException {
        Random random = new Random();
        int x = random.nextInt(16);
        int y = random.nextInt(16);
        String coordinate = (char) ('a' + y) + "" + x;
        return parseCoords(coordinate);
    }

    private void addShips(Field field) {
        boolean valid = false;
        while (!valid) {
            try {
                field.addShip(getCoords());
                valid = true;
            } catch (Exception e) {
                System.out.print("Wrong coordinates. Try it again: ");
            }
        }
    }
}
