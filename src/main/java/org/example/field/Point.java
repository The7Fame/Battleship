package org.example.field;

import org.example.ship.Ship;

import static org.example.field.State.*;

public class Point {

    private final Ship ship;
    private State state;
    public Point() {
        this.ship = null;
        this.state = EMPTY;
    }

    public Point(Ship ship) {
        this.ship = ship;
        this.state = SHIP;
    }
    public State attack() {
        if (this.state == EMPTY) {
            this.state = MISS;
        } else if (this.state == SHIP) {
            this.ship.attackShip();
            if (this.ship.isAlive()) {
                this.state = DEAD;
            } else {
                this.state = HIT;
            }
        }
        return this.state;
    }

    public State getStatus() {
        return this.state;
    }

}
