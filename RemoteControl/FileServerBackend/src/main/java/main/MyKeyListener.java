package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GUI.terminal;

public class MyKeyListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String[] terminalContains = terminal.getText().split("\n");
            String[] command = terminalContains[terminalContains.length - 1].split(" "); // socketId command argument(s)
            ConnectionHandler handler = Main.handlers.get(Integer.parseInt(command[0].split("_")[1]));
            handler.command = command;
            try {
                handler.executeCommand();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
