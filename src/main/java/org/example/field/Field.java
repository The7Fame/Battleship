package org.example.field;

import org.example.ship.*;

import java.util.HashMap;

import static org.example.field.State.*;

public class Field {

    private static final int SIZE = 16;
    private Point[][] cells;
    private int liveCells;
    private HashMap<Integer, Integer> ships;
    public Field() {
        initialize();
        this.liveCells = 0;
    }

    private void initialize() {
        this.cells = new Point[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                cells[x][y] = new Point();
            }
        }
        this.ships = new HashMap<>();
        for(int i = 1; i < 7; i++){
            int ship_count = 7 - i;
            ships.put(i, ship_count);
        }
    }

    public String drawField(boolean hide) {
        StringBuilder builder = new StringBuilder();
        builder.append("  A B C D E F G H I J K L M N O P\n");
        for (int x = 0; x < SIZE; x++) {
            builder.append((x +1)).append(" ");
            for (int y = 0; y < SIZE; y++) {
                if (hide) {
                    builder.append(this.cells[x][y].getStatus().getPublicValue());
                } else {
                    builder.append(this.cells[x][y].getStatus().getPrivateValue());
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public int getLiveCells() {
        return liveCells;
    }

    public State attack(String[] coordinates) {
        int[][] numbers = parseData(coordinates[0]);

        State currentState = this.cells[numbers[0][0]][numbers[0][1]].getStatus();
        State afterAttackState = this.cells[numbers[0][0]][numbers[0][1]].attack();
        if (currentState == SHIP && (afterAttackState == HIT || afterAttackState == DEAD)) {
            this.liveCells--;
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

    public HashMap<Integer, Integer> getShips() {
        return ships;
    }

    public void addShip(String... coordinates) {
        Ship ship = choiceOfShip(coordinates.length);

        int[][] numbers = parseData(coordinates);
        if (this.ships.get(ship.getSize()) != 0 && isFreeCells(numbers) && isValidPlace(numbers)) {
            for (int[] number : numbers) {
                this.cells[number[0]][number[1]] = new Point(ship);
            }
            this.liveCells += ship.getSize();
            this.ships.replace(ship.getSize(), this.ships.get(ship.getSize()) - 1);
        } else {
            System.out.println("Wrong coordinates");
        }
    }

    private int[][] parseData(String... coordinates) {
        int[][] positions = new int[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            positions[i][0] = Integer.parseInt(coordinates[i].substring(1)) - 1;
            positions[i][1] = coordinates[i].charAt(0) - 97;
            if (positions[i][0] < 0 || positions[i][0] > 15 || positions[i][1] < 0 || positions[i][1] > 15) {
                System.out.println("Wrong coordinates");
            }
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
                if (this.cells[x][y].getStatus() != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
