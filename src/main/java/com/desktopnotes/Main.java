package com.desktopnotes;

import com.desktopnotes.model.Note;
import com.desktopnotes.storage.NoteStorage;
import com.desktopnotes.ui.NoteWindow;

import java.io.IOException;
import java.util.List;

public class Main {
    static void main() {
        NoteStorage noteStorage = new NoteStorage();
        try{
            List<Note> noteList = noteStorage.loadAll();
            if(noteList.isEmpty()){
                Note note = new Note("","");
                NoteWindow noteWindow = new NoteWindow(note);
                try{
                    noteStorage.save(note);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                for(Note note: noteList){
                    NoteWindow noteWindow = new NoteWindow(note);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
