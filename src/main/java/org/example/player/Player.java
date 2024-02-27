package org.example.player;

import org.example.field.Field;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    private final String name;
    private Socket socket;
    private Player enemy;

    private BufferedReader in;
    private PrintWriter out;
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

    public Player(String name, Socket socket) throws IOException{
        this.name = name;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
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

    public void setEnemy(Player enemy){
        this.enemy = enemy;
    }
    public Player getEnemy(){
        return this.enemy;
    }

    public void setField(Field field) {
        this.field = field;
    }

}
