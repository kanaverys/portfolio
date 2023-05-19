import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Map;

public class Storage {

    public static final String DOCKER_ADDRESS = "redis://127.0.0.1:6379";
    public RedissonClient redissonClient;
    public RMap<String, Map<String, Integer>> studentsMap;

    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress(DOCKER_ADDRESS);
        try {
            redissonClient = Redisson.create(config);
        }
        catch (Exception Exc) {
            System.out.println("Failed to connect to Redis");
            System.out.println(Exc.getMessage());
        }

        studentsMap = redissonClient.getMap("students");
    }

    public void printAll() {
        studentsMap.readAllEntrySet().forEach(System.out::println);
    }

    public void print(String name){
        System.out.println(studentsMap.get(name));
    }

    public void remove(String name){
        studentsMap.remove(name);
    }

    public void removeAll(){
        for (String s : studentsMap.keySet()) {
            studentsMap.remove(s);
        }
    }

    public void increment(String student, String course, int inc) {
        Map<String, Integer> studentCoursesMap = studentsMap.get(student);

        studentCoursesMap.put(course, studentCoursesMap.get(course) + inc);
        studentsMap.remove(student);
        studentsMap.put(student, studentCoursesMap);
    }
}