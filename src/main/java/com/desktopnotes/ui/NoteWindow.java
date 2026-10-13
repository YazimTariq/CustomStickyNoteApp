package com.desktopnotes.ui;

import com.desktopnotes.model.Note;
import com.desktopnotes.storage.NoteStorage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class NoteWindow extends JFrame {

    private Timer timer;
    private static List<NoteWindow> openWindows = new ArrayList<>();


    public NoteWindow(Note note) {
        setupWindow();
        createHeader(note);
        setupAutoSave(note);
        createTextArea(note);
        openWindows.add(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                openWindows.remove(NoteWindow.this);
            }
        });
        setBounds(note.getX(),note.getY(),note.getWidth(),note.getHeight());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle rectangle = getBounds();
                note.setX(rectangle.x);
                note.setY(rectangle.y);
                note.setWidth(rectangle.width);
                note.setHeight(rectangle.height);
                timer.restart();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                Rectangle rectangle = getBounds();
                note.setX(rectangle.x);
                note.setY(rectangle.y);
                note.setWidth(rectangle.width);
                note.setHeight(rectangle.height);
                timer.restart();
            }
        });
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
            Note newNote = new Note("", "");
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
                if(note.getTitle().isEmpty() && note.getContent().isEmpty())
                    return;
                storage.save(note);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        timer.setRepeats(false);
    }

    private void positionNewWindow(NoteWindow newWindow){
        //Gets the screen dimensions x y
        Rectangle bounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        //Gets points of current window
        Point topLeftPoint = getLocation();
        Point topRightPoint = new Point(topLeftPoint.x + getWidth() + 5, topLeftPoint.y);
        Point bottomLeftPoint = new Point(topLeftPoint.x, topLeftPoint.y + getHeight() + 5);
        Point bottomRightPoint = new Point(topRightPoint.x, bottomLeftPoint.y);
        Point leftLeftPoint = new Point(topLeftPoint.x - (getWidth() + 5), topLeftPoint.y);

        int availableRight = bounds.x + bounds.width - topRightPoint.x;
        int availableBottom = bounds.y + bounds.height - bottomLeftPoint.y;
        int availableTop = topLeftPoint.y - bounds.y;
        int availableLeft = topLeftPoint.x - bounds.x;

        //checks if right area has space, yes -> add there
        if(availableRight >= newWindow.getWidth() &&
                isPositionAvailable(newWindow, topRightPoint.x, topRightPoint.y)){
            newWindow.setLocation(topRightPoint.x,topRightPoint.y);
        }
        //no -> check bottom -> if bottom has space, yes -> add there
        else if(availableBottom >= newWindow.getHeight() &&
                isPositionAvailable(newWindow, bottomLeftPoint.x, bottomLeftPoint.y)){
            newWindow.setLocation(bottomLeftPoint.x, bottomLeftPoint.y);
        }
        //no -> check top -> if top has space, yes -> add there
        else if(availableTop >= newWindow.getHeight() &&
                isPositionAvailable(newWindow, topLeftPoint.x, topLeftPoint.y - newWindow.getHeight())){
            newWindow.setLocation(topLeftPoint.x, topLeftPoint.y-newWindow.getHeight());
        }
        //no -> check left -> if left has space, yes -> add there
        else if(availableLeft >= newWindow.getWidth() &&
                isPositionAvailable(newWindow, topLeftPoint.x - newWindow.getWidth(),topLeftPoint.y)){
            newWindow.setLocation(topLeftPoint.x - newWindow.getWidth(), topLeftPoint.y);
        }
        //no -> check top right -> if top right has space, yes -> add there
        else if(availableRight >= newWindow.getWidth() &&
                availableTop >= newWindow.getHeight() &&
                isPositionAvailable(newWindow, topRightPoint.x, topRightPoint.y - (newWindow.getHeight() + 5))){
            newWindow.setLocation(topRightPoint.x, topRightPoint.y - (newWindow.getHeight() + 5));
        }
        //no -> check bottom right -> if bottom right has space, yes -> add there
        else if(availableRight >= newWindow.getWidth() &&
                availableBottom >= newWindow.getHeight() &&
                isPositionAvailable(newWindow, bottomRightPoint.x, bottomRightPoint.y)){
            newWindow.setLocation(bottomRightPoint.x, bottomRightPoint.y);
        }
        //no -> check top left -> if top left has space, yes -> add there
        else if(availableTop >= newWindow.getHeight() &&
                availableLeft >= newWindow.getWidth() &&
                isPositionAvailable(newWindow, leftLeftPoint.x, leftLeftPoint.y - newWindow.getHeight())){
            newWindow.setLocation(leftLeftPoint.x, leftLeftPoint.y - (newWindow.getHeight() + 5));
        }
        //no -> check bottom left -> if bottom left has space, yes -> add there
        else if(availableBottom >= newWindow.getHeight() &&
                availableLeft >= newWindow.getWidth() &&
                isPositionAvailable(newWindow, leftLeftPoint.x, leftLeftPoint.y + newWindow.getHeight() + 5)) {
            newWindow.setLocation(leftLeftPoint.x, leftLeftPoint.y + newWindow.getHeight());
        }
        //no for all
        else{
            newWindow.setLocation(topLeftPoint.x + 15, topLeftPoint.y + 15);
        }

    }

    private boolean isPositionAvailable(NoteWindow newWindow, int x, int y){
        Rectangle newWindowBox = new Rectangle(
                x,y,newWindow.getWidth(),newWindow.getHeight());
        for(NoteWindow noteWindow : openWindows){
            if(noteWindow == newWindow)
                continue;
            int x1 = noteWindow.getX();
            int y1 = noteWindow.getY();
            Rectangle oldWindowBox = new Rectangle(x1,y1, noteWindow.getWidth(), noteWindow.getHeight());
            if(newWindowBox.intersects(oldWindowBox))
                return false;
        }
        return true;
    }

}
