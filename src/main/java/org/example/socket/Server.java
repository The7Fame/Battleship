package org.example.socket;

import org.example.field.Field;
import org.example.game.Game;
import org.example.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public void start() throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server listening on port 5000\n You need to launch game again to connect to the server\nRemember, that all ships will be setting up automatically");
        List<Player> players = new ArrayList<>();
        
        while (true){
            Socket socket = server.accept();
            System.out.println("Connected " + socket.getRemoteSocketAddress());
            Player player = new Player("NetworkPLAYER", socket);
            player.setField(putShipsAutomatically());
            players.add(player);
            if(players.size() == 2){
                players.get(0).setEnemy(players.get(1));
                players.get(1).setEnemy(players.get(0));
                Thread thread = new Thread(new Game(players));
                thread.start();
                players = new ArrayList<>();
            }else {
                player.getOut().println("Waiting for other players to join...");
            }
        }
    }

    private Field putShipsAutomatically(){
        Field field = new Field();
        while (field.getLivePoints() != 56){
            field.setShipsAutomatically();
        }
        return field;
    }
}