package com.desktopnotes.storage;

import com.desktopnotes.model.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NoteStorage {
    public void save(Note note) throws IOException {
        String appData = System.getenv("LOCALAPPDATA");
        Path path = Paths.get(appData, "DesktopNotes", "note.txt");
        Files.createDirectories(path.getParent());
        Files.writeString(path, note.getTitle() + "\n" + note.getContent());
    }
}
