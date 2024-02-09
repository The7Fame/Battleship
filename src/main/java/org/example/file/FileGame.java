package org.example.file;
import java.io.File;
import java.io.FileWriter;

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

    public FileWriter getWriter() throws IOException {
        return this.writer; 
    }
}