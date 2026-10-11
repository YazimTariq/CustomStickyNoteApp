package com.desktopnotes;

import com.desktopnotes.model.Note;
import com.desktopnotes.storage.NoteStorage;
import com.desktopnotes.ui.NoteWindow;

import java.io.IOException;

public class Main {
    static void main() {
        Note note = new Note("My note", "First note");
        NoteWindow noteWindow = new NoteWindow(note);
        NoteStorage noteStorage = new NoteStorage();

        try{
            noteStorage.save(note);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
