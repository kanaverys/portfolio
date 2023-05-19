import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Storage storage = new Storage();
        storage.init();

        Scanner scanner = new Scanner(System.in);
        while(true){
            String input = scanner.nextLine();
            String[] commands = input.split(" ");
            if(input.equals("stop")) break;
            if(input.matches("put .+ .+ .+")){
                Map<String, Integer> courses = new HashMap<>();
                courses.put(commands[2], Integer.parseInt(commands[3]));
                storage.studentsMap.fastPut(commands[1], courses);
            }
            if(input.matches("increment .+ .+ .+")){
                storage.increment(commands[1], commands[2], Integer.parseInt(commands[3]));
            }
            if(input.matches("remove .+")){
                storage.remove(commands[1]);
            }
            if(input.equals("print all")){
                storage.printAll();
            }
            if(input.equals("remove all")) storage.removeAll();
            else if(input.matches("print .+")){
                storage.print(commands[1]);
            }
        }
    }
}