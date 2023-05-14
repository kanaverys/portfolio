package main;

import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Date;

public class ConnectionHandler implements Runnable{
    int id;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;
    String[] header;
    String[] command;
    boolean breakFlag;
    Date date;

    public ConnectionHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            date = new Date();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            header = dataInputStream.readUTF().split("_");

            GUI.terminal.append(date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + " started\n");

            while(true){
                command = dataInputStream.readUTF().split(" ");
                respond();
                if(breakFlag){
                    break;
                }
            }

            GUI.terminal.append(date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + " closed\n");
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            Main.handlers.remove(this.id);
        }
        catch (Exception ex){
            if(breakFlag){
                GUI.terminal.append(date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + " closed\n");
            }
            else{
                GUI.terminal.append(date.toString() + "> <ERROR> Connection with " + header[0] + '_' + id + " closed due to exception\n");
                ex.printStackTrace();
            }
        }
    }

    private void respond() throws Exception{
        command = dataInputStream.readUTF().split(" ");
        if(command[0].equalsIgnoreCase("write")){
            String filename = command[1];
            int fileContentLength = dataInputStream.readInt();
            byte[] fileContent = new byte[fileContentLength];
            dataInputStream.readFully(fileContent);
            File input = new File("src/main/resources/" + header[0] + '_' + id +'/' + filename);
            FileUtils.writeByteArrayToFile(input, fileContent);
            if(input.isDirectory()) input.mkdirs();
            GUI.terminal.append(date.toString() + "> <INFO> Received data from " + header[0] + '_' + id + '\n');
        }
    }

    public void executeCommand() throws Exception{
        if(command[1].equalsIgnoreCase("write")){ // username write src dst
            String path = command[2];
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            byte[] bytes = fileInputStream.readAllBytes();
            dataOutputStream.writeUTF("write " + command[3]);
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes);
            GUI.terminal.append(date.toString() + "> <INFO> Sent data to " + header[0] + '_' + id + '\n');
        }

        if(command[1].equalsIgnoreCase("read")){ // username read src
            dataOutputStream.writeUTF("read " + command[2]);
        }

        if(command[1].equalsIgnoreCase("close")){ // username close
            breakFlag = true;
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }

        if(command[1].equalsIgnoreCase("execute")){ // username execute path
            dataOutputStream.writeUTF("execute " + command[2]);
        }

        if(command[1].equalsIgnoreCase("dir")){ // username dir path
            dataOutputStream.writeUTF("dir " + command[2]);
            String[] fileNames = dataInputStream.readUTF().split(",");
            for (String fileName : fileNames) {
                System.out.println(fileName);
            }
        }
    }
}