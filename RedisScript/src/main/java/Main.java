import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        Storage storage = new Storage();
        storage.init();

        Map<String, Integer> courses = new HashMap<>();
        courses.put("Web dev", 1);
        courses.put("Data Science", 4);
        storage.studentsMap.fastPut("Ivanov I.I.", courses);

        storage.printAll();

        storage.increment("Ivanov I. I.", "Data Science", 100);

        storage.printAll();
    }
}