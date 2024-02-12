package org.example.file;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileGame{
    private File file;
    private FileWriter writer;

    public FileGame(String fileName){
        this.file= new File(fileName); 
        try {
            this.writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameStart() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String gameStartedAt = formatter.format(dateTime);
        writer.write("Game started at " + gameStartedAt +"\n");
        writer.flush();
    }

    public void gameEnd(String name) throws IOException {
        writer.write(name.toUpperCase() + " won");
        writer.close();
    }

    public void writeMessage(String string) throws IOException {
        writer.write(string);
        writer.flush();
    }
}