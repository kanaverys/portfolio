package main;

import javax.swing.*;
import java.awt.*;

public class GUI {
    static JTextArea terminal;
    public GUI(){
        JFrame mainFrame = new JFrame("Server terminal");
        mainFrame.setSize(800, 500);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        terminal = new JTextArea();
        terminal.setBackground(Color.BLACK);
        terminal.setForeground(Color.WHITE);
        terminal.setCaretColor(Color.WHITE);
        terminal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        terminal.addKeyListener(new MyKeyListener());
        mainFrame.add(terminal);

        mainFrame.setVisible(true);
    }
}