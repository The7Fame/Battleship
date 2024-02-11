package org.example.field;

import org.example.ship.*;

import java.util.*;

import static org.example.field.Constants.POSITION;
import static org.example.field.Constants.SIZE;
import static org.example.field.State.*;

public class Field {

    private Point[][] points;
    private int livePoints;
    private HashMap<Integer, Integer> ships;
    public Field() {
        initialize();
        this.livePoints = 0;
    }

    private void initialize() {
        this.points = new Point[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                points[x][y] = new Point();
            }
        }
        this.ships = new HashMap<>();
        for(int i = 1; i < 7; i++){
            int shipCount = 7 - i;
            ships.put(i, shipCount);
        }
    }

    public String[] drawField(boolean hide) {
        String[] lines = new String[SIZE + 1];
        StringBuilder builder = new StringBuilder();
        builder.append("  ");
        for (int i = 0; i < SIZE; i++) {
            builder.append((char) ('A' + i)).append(" ");
        }
        lines[0] = builder.toString();
        builder.setLength(0);
        for (int x = 0; x < SIZE; x++) {
            builder.append((x + 1)).append(" ");
            for (int y = 0; y < SIZE; y++) {
                if (hide) {
                    builder.append(this.points[x][y].getStatus().getPublicValue());
                } else {
                    builder.append(this.points[x][y].getStatus().getPrivateValue());
                }
                builder.append(" ");
            }
            lines[x + 1] = builder.append("|").toString();
            builder.setLength(0);
        }
        return lines;
    }

    public int getLivePoints() {
        return livePoints;
    }

    public State attack(String[] coordinates) {

        int[][] numbers = parseData(coordinates[0]);

        State currentState = this.points[numbers[0][0]][numbers[0][1]].getStatus();
        State afterAttackState = this.points[numbers[0][0]][numbers[0][1]].attack();
        if (currentState == SHIP && (afterAttackState == HIT || afterAttackState == DEAD)) {
            this.livePoints--;
        }
        return afterAttackState;
    }

    public Ship choiceOfShip(int size){
        Ship ship = null;
        switch (size) {
            case 6:
                ship = new Six();
                break;
            case 5:
                ship = new Five();
                break;
            case 4:
                ship = new Four();
                break;
            case 3:
                ship = new Three();
                break;
            case 2:
                ship = new Two();
                break;
            case 1:
                ship = new One();
                break;
            default:  break;
        }
        return ship;
    }

    public void addShip(String... coordinates) {
        Ship ship = choiceOfShip(coordinates.length);
        int[][] numbers = parseData(coordinates);
        if (this.ships.get(ship.getSize()) != 0 && isFreeCells(numbers) && isValidPlace(numbers)) {
            for (int[] number : numbers) {
                this.points[number[0]][number[1]] = new Point(ship);
            }
            this.livePoints += ship.getSize();
            this.ships.replace(ship.getSize(), this.ships.get(ship.getSize()) - 1);
        } else {
            return;
        }
    }

    private int[][] parseData(String... coordinates) {
        int[][] positions = new int[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            positions[i][0] = Integer.parseInt(coordinates[i].substring(1)) - 1;
            positions[i][1] = coordinates[i].charAt(0) - 97;
        }
        return positions;
    }

    private boolean isFreeCells(int[][] coordinates) {
        int[] firstCoordinates = coordinates[0];
        for (int i = 1; i < coordinates.length; i++) {
            if (!(coordinates[i][0] == firstCoordinates[0] &&
                    (coordinates[i][1] == firstCoordinates[1] + i || coordinates[i][1] == firstCoordinates[1] - i)
                    || coordinates[i][1] == firstCoordinates[1] &&
                    (coordinates[i][0] == firstCoordinates[0] + i || coordinates[i][0] == firstCoordinates[0] - i))
            ) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPlace(int[][] coordinates) {
        int sX = Math.max(coordinates[0][0] - 1, 0);
        int sY = Math.max(coordinates[0][1] - 1, 0);
        int eX = Math.min(coordinates[coordinates.length - 1][0] + 1, SIZE - 1);
        int eY = Math.min(coordinates[coordinates.length - 1][1] + 1, SIZE - 1);
        for (int x = sX; x <= eX; x++) {
            for (int y = sY; y <= eY; y++) {
                if (this.points[x][y].getStatus() != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setShipsAutomatically(){
        Random random = new Random();
        for(int size = 6; size >= 1; size--){
            while (ships.get(size) != 0){
                String[] coordinates = new String[size];
                int x = random.nextInt(16);
                int y = random.nextInt(16);
                int indexPosition = random.nextInt(POSITION.length);
                char randomPosition = POSITION[indexPosition];
                for (int i = 0; i < size; i++){
                    switch (randomPosition){
                        case 'v':
                            coordinates[i] = (char) ('a' + y) + "" + (x + i + 1);
                            break;
                        case 'h':
                            coordinates[i] = (char) ('a' + y + i) + "" + (x + 1);
                            break;
                    }
                }
                boolean valid = false;
                while (!valid) {
                    try {
                        addShip(coordinates);
                        valid = true;
                    } catch (Exception e) {
                        valid=true;
                    }
                }
            }
        }
    }
}
