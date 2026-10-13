package com.desktopnotes.storage;

import com.desktopnotes.model.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class NoteStorage {
    public void save(Note note) throws IOException {
        String appData = System.getenv("LOCALAPPDATA");
        Path path = Paths.get(appData, "DesktopNotes", note.getId() + ".txt");
        Files.createDirectories(path.getParent());
        Files.writeString(path,
                note.getId()+ "\n" +
                note.getTitle() + "\n" +
                note.getX() + "\n" +
                note.getY() + "\n" +
                note.getWidth() + "\n" +
                note.getHeight() + "\n" +
                "---" + "\n" +
                note.getContent());
    }

    public Note load(String oldId) throws IOException{
        String appData = System.getenv("LOCALAPPDATA");

        Path path = Paths.get(appData, "DesktopNotes", oldId + ".txt");
        String data = Files.readString(path);
        int seperator = data.indexOf("---");
        int newLine = data.indexOf("\n",seperator);
        String metaData = data.substring(0, seperator);
        String content = data.substring(newLine+1);

        String[] part = metaData.split("\\R");
        Note note = new Note(part[0],part[1],Integer.parseInt(part[2]),Integer.parseInt(part[3]),Integer.parseInt(part[4]),Integer.parseInt(part[5]),content);
        return note;
    }

    public List<Note> loadAll() throws IOException {
        String appData = System.getenv("LOCALAPPDATA");
        Path directory = Paths.get(appData, "DesktopNotes");
        List<Note> notes = new ArrayList<>();

        if (!Files.exists(directory)) {
            return notes;
        }

        try(var paths = Files.list(directory)){
            List<Path> pathsList = paths.filter(
                    path -> path.toString().endsWith(".txt")).toList();
            for(Path path: pathsList){
                String id = path.getFileName().toString();
                id = id.substring(0,id.length() - 4);
                notes.add(load(id));
            }
        }
        return notes;
    }
}
