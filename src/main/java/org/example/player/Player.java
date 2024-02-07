package org.example.player;

import org.example.field.Field;


public class Player {

    private final String name;
    private Field field;
    private boolean bot;

    public Player(String name) {
        this.name = name;
        this.bot = false;
    }

    public Player(){
        this.name = "Bot";
        this.bot = true;
    }

    public boolean isBot() {
        return bot;
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
