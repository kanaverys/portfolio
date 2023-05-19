package main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;

public class ConnectionHandler implements Runnable{
    int id;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;
    String[] header;
    String[] command;
    boolean breakFlag;
    boolean dirFailure;
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

            GUI.terminal.append(date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + " started on IP" + socket.getInetAddress().toString() + '\n');

            while(true){
                command = dataInputStream.readUTF().split(" ");
                respond();
                if(breakFlag){
                    break;
                }
            }

            GUI.terminal.append('\n' + date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + "on IP " + socket.getInetAddress().toString() + " closed\n");
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            Main.handlers.remove(this.id);
        }
        catch (Exception ex){
            if(breakFlag && !dirFailure){
                GUI.terminal.append('\n' + date.toString() + "> <INFO> Connection with " + header[0] + '_' + id + " on IP " + socket.getInetAddress().toString() + " closed\n");
            }
            if(dirFailure){
                GUI.terminal.append('\n' + date.toString() + "> <ERROR> DIR command execution " + header[0] + " on IP " + socket.getInetAddress().toString() + " unsuccessful\n");
                try{
                    socket.setSoTimeout(Integer.MAX_VALUE);
                }
                catch (Exception se){
                    se.printStackTrace();
                }
            }
            else{
                try{
                    socket.setSoTimeout(Integer.MAX_VALUE);
                    if(dirFailure) GUI.terminal.append('\n' + date.toString() + "> <ERROR> DIR command execution " + header[0] + " on IP " + socket.getInetAddress().toString() + " unsuccessful\n");
                    else GUI.terminal.append('\n' + date.toString() + "> <ERROR> Connection with " + header[0] + '_' + id + " on IP " + socket.getInetAddress().toString() + " closed due to exception\n");
                    ex.printStackTrace();
                }
                catch (Exception ignored){
                    try{
                        socket.close();
                        dataOutputStream.close();
                        dataInputStream.close();
                    }
                    catch (IOException io){
                        io.printStackTrace();
                    }
                }
            }
        }
    }

    private void respond() throws Exception{
        socket.setSoTimeout(2000);
        command = dataInputStream.readUTF().split(" ");
        if(command[0].equalsIgnoreCase("write")){
            String filename = command[1];
            int fileContentLength = dataInputStream.readInt();
            byte[] fileContent = new byte[fileContentLength];
            dataInputStream.readFully(fileContent);
            File input = new File("src/main/resources/" + header[0] + '_' + id +'/' + filename);
            FileUtils.writeByteArrayToFile(input, fileContent);
            if(input.isDirectory()) input.mkdirs();
            GUI.terminal.append('\n' + date.toString() + "> <INFO> Received data from " + header[0] + '_' + id + " on IP " + socket.getInetAddress().toString() + '\n');
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
            GUI.terminal.append('\n' + date.toString() + "> <INFO> Sent data to " + header[0] + '_' + id + " on IP " + socket.getInetAddress().toString() + '\n');
        }

        if(command[1].equalsIgnoreCase("read")){ // username read src
            dataOutputStream.writeUTF("read " + command[2]);
            try{
                respond();
            }
            catch (SocketTimeoutException se){
                GUI.terminal.append('\n' + date.toString() + "> <ERROR> Read from " + header[0] + "on IP " + socket.getInetAddress().toString() + " unsuccessful\n");
            }
        }

        if(command[1].equalsIgnoreCase("restart")){ // username restart
            breakFlag = true;
            socket.close();
            dataOutputStream.close();
            dataInputStream.close();
        }

        if(command[1].equalsIgnoreCase("close")){ //username close
            breakFlag = true;
            dataOutputStream.writeUTF("close");
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        }

        if(command[1].equalsIgnoreCase("execute")){ // username execute path
            dataOutputStream.writeUTF("execute " + command[2]);
        }

        if(command[1].equalsIgnoreCase("dir")){ // username dir path
            socket.setSoTimeout(2000);
            try{
                dataOutputStream.writeUTF("dir " + command[2]);
                String[] fileNames = dataInputStream.readUTF().split(",");
                for (String fileName : fileNames) {
                    GUI.terminal.append('\n' + fileName);
                }
            }
            catch (Exception exception){
                dirFailure = true;
            }
        }
    }
}