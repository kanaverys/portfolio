package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.SocketException;

import static main.GUI.terminal;

public class MyKeyListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String[] terminalContains = terminal.getText().split("\n");
            String[] command = terminalContains[terminalContains.length - 1].split(" "); // socketId command argument(s)
            ConnectionHandler handler = Main.handlers.get(Integer.parseInt(command[0].split("_")[1]));
            try {
                handler.command = command;
                handler.executeCommand();
            }
            catch (Exception ex) {
                try{
                    handler.socket.setSoTimeout(Integer.MAX_VALUE);
                }
                catch (SocketException se){
                    se.printStackTrace();
                }
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
