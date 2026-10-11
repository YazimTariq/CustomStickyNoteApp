package com.desktopnotes.ui;

import com.desktopnotes.model.Note;
import com.desktopnotes.storage.NoteStorage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;

public class NoteWindow extends JFrame {

    private Timer timer;

    public NoteWindow(Note note) {
        setupWindow();
        createHeader(note);
        setupAutoSave(note);
        createTextArea(note);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("StickyNote");
        setSize(300, 300);
        setLocation(100, 100);
        setAlwaysOnTop(true);
    }

    private void createHeader(Note note) {
        JLabel label = new JLabel(note.getTitle());
        JButton button = new JButton("+");
        JPanel panel = new JPanel();

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(250, 198, 17));
        button.addActionListener(event -> {
            Note newNote = new Note("New Note", "");
            NoteWindow newNoteWindow = new NoteWindow(newNote);
            positionNewWindow(newNoteWindow);

        });

        Font font = new Font("Arial", Font.BOLD, 14);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.setBackground(new Color(245, 229, 120));
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(button, BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
    }

    private void createTextArea(Note note) {
        JTextArea textArea = new JTextArea(note.getContent());
        textArea.setBackground(new Color(255, 249, 168));
        textArea.setForeground(Color.black);
        Insets insets = new Insets(5, 5, 5, 5);
        textArea.setMargin(insets);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        textArea.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        note.setContent(textArea.getText());
                        timer.restart();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        note.setContent(textArea.getText());
                        timer.restart();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        note.setContent(textArea.getText());
                        timer.restart();
                    }
                }
        );
    }

    private void setupAutoSave(Note note) {
        NoteStorage storage = new NoteStorage();
        timer = new Timer(1000, event -> {
            try {
                storage.save(note);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        timer.setRepeats(false);
    }

    private void positionNewWindow(NoteWindow newWindow){
        Rectangle bounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        Point point = getLocation();

        int width = getWidth();
        int height = getHeight();

        int newX = point.x + width + 5;
        int newY = point.y;

        if(newY < 0)
            newY = 0;

        if(newX < 0)
            newX = 0;

        if(newY + newWindow.getHeight() > bounds.height)
            newY = point.y - (newWindow.getHeight() + 5);

        if(newX + newWindow.getWidth() >= bounds.width)
            newX = point.x - (newWindow.getWidth() + 5);

        newX = Math.clamp(newX, 0, bounds.width - newWindow.getWidth());
        newY = Math.clamp(newY, 0, bounds.height - newWindow.getHeight());


        newWindow.setLocation(point.x + 5 + width, point.y);
    }

}
