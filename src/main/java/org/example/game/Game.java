package org.example.game;

import org.example.field.Field;
import org.example.field.State;
import org.example.player.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static org.example.field.Constants.*;

public class Game {
    private final Scanner input;
    private Player player;
    private Player enemy;
    private int turn;

    private final ArrayList<String> randomShips = new ArrayList<>(); // ships which were generated
    public Game(Scanner input) {
        this.turn = 0;
        this.input = input;
    }

    public void play() {
        createPlayers();

        Field playerField = new Field();
        Field enemyField = new Field();
        System.out.println(player.getName() + ", your turn to set up ships");
        player.setField(placeShips(playerField, player.isBot()));
        System.out.println(enemy.getName() + ", your turn to set up ships");
        enemy.setField(placeShips(enemyField, enemy.isBot()));

        while (playerField.getLiveCells() > 0 && enemyField.getLiveCells() > 0) {
            if (turn == 0) {
                System.out.println(player.getName() + ", your turn to attack");
                attack(playerField, enemyField, player.isBot());
            } else {
                System.out.println(enemy.getName() + ", your turn to attack");
                attack(enemyField, playerField, enemy.isBot());
            }
        }
        winner();
    }

    public void msgWelcome() {
        System.out.println(
                """
                Welcome to Battleship!
                Coordinates input example (a1 h 5)
                a1 - coordinate, h - position, 5 - size of the ship
                """
        );
    }

    private void createPlayers() {
        System.out.print("Name of the first player: ");
        this.player = new Player(this.input.nextLine());
        System.out.println("With whom do you want to play\n0 - human\n1 - computer");
        String choice = this.input.nextLine();
        if(choice.equals("0")){
            System.out.print("Name of the second player: ");
            this.enemy = new Player(this.input.nextLine());
        }else if(choice.equals("1")){
            this.enemy = new Player();
        }
    }

    private void winner() {
        if (this.player.getField().getLiveCells() <= 0) {
            System.out.print(this.enemy.getName() + ", your are the winner");
        } else {
            System.out.print( this.player.getName() + ", your are the winner");
        }
    }
    private void attack(Field myField, Field enemyField, boolean bot) {
        System.out.print("        Your field\n"
                + myField.drawField(false)
                + "        Field to attack\n"
                + enemyField.drawField(true)
        );
        switch (attackAgain(enemyField, bot)) {
            case HIT -> {
                System.out.println("Stricked");
            }
            case DEAD -> {
                System.out.println("Destroyed");
            }
            default -> {
                System.out.println("Miss");
                this.turn = (this.turn + 1) % 2;
            }
        }
    }

    private State attackAgain(Field field, boolean bot) {
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
        if (!bot){
            System.out.println(field.drawField(false));
            System.out.println("How do your want do set up ships?\n0 - manually\n1 - automatically");
            String choice = this.input.nextLine();
            if(choice.equals("0")){
                while (field.getLiveCells() != 56) {
                    System.out.print("Put a ship on your board ");
                    addShipAgain(field, false);
                    System.out.println(field.drawField(false));
                }
            } else if (choice.equals("1")) {
                while (field.getLiveCells() != 56) {
                    addShipAgain(field, true);
                }
            }
            return field;
        }
        while (field.getLiveCells() != 56) {
            addShipAgain(field, true);
        }
        return field;
    }

    private String[] parseCoords(String coords){
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
            coordinates = new String[1];
            for (int i = 0; i < 1; i++) {
                coordinates[i] = c[i];
            }
        }
        return coordinates;
    }
    private String[] getCoords() {
        String coords = this.input.nextLine();
        return parseCoords(coords);
    }

    private String[] randomCoordsToAttack(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int indexChar = random.nextInt(CHARS.length);
        char randomChar = CHARS[indexChar];
        int indexRaw = random.nextInt(RAWS.length);
        int randomRaw = RAWS[indexRaw];
        sb
                .append(randomChar)
                .append(randomRaw);
        System.out.println(sb.toString());
        return parseCoords(sb.toString());

    }
    private String[] randomCoordsToSetUpShip(){
        String coords;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder();
            int indexChar = random.nextInt(CHARS.length);
            char randomChar = CHARS[indexChar];
            int indexRaw = random.nextInt(RAWS.length);
            int randomRaw = RAWS[indexRaw];
            int indexPosition = random.nextInt(POSITION.length);
            char randomPosition = POSITION[indexPosition];
            int indexShip = random.nextInt(SHIPS.length);
            int randomShip = SHIPS[indexShip];
            sb
                    .append(randomChar)
                    .append(randomRaw)
                    .append(" ")
                    .append(randomPosition)
                    .append(" ")
                    .append(randomShip);
            coords = sb.toString();
        }while (this.randomShips.contains(coords));
        this.randomShips.add(coords);
        return parseCoords(coords);
    }
    private void addShipAgain(Field field, boolean auto) {
        boolean valid = false;
        while (!valid) {
            try {
                field.addShip(auto ? randomCoordsToSetUpShip() : getCoords());
                valid = true;
            } catch (Exception e) {
                System.out.print("Wrong coordinates. Try it again: ");
            }
        }
    }
}
