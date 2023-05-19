package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    static HashMap<Integer, ConnectionHandler> handlers = new HashMap<>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        final Socket[] socket = new Socket[1];
        new GUI();

        try{
            serverSocket = new ServerSocket(1234);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try{
                        socket[0] = serverSocket.accept();
                        ConnectionHandler newHandler = new ConnectionHandler(socket[0]);
                        newHandler.id = (int)(Math.random() * 100000d);
                        new Thread(newHandler).start();
                        handlers.put(newHandler.id, newHandler);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
