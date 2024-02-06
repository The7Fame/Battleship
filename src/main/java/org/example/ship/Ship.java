package org.example.ship;


public abstract class Ship{
    private final int size;
    private int shots;

    Ship(int size){
        this.size = size;
        this.shots = 0;
    }

    public void attackShip(){
        shots++;
    }

    public boolean isAlive(){
        return shots == size;
    }

    public int getSize() {
        return size;
    }
}
