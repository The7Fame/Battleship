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

    public FileWriter getWriter() {
        return writer;
    }
}