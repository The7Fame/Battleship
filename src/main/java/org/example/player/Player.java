package org.example.player;

import org.example.field.Field;


public class Player {

    private final String name;
    private Field field;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

}
