package org.example.player;

import org.example.field.Field;

/***
 * Сущность игрока.
 * Каждый игрок имеет имя, по которому к нему можно обращаться.
 * За каждым игроком закреплена доска, на которой расположены его корабли.
 */
public class Player {

    /***
     * Имя игрока.
     */
    private final String name;
    /***
     * Доска игрока.
     */
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
