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

    public boolean increment(String student, String course, int inc) {
        if (!studentsMap.containsKey(student) || inc <= 0) {
            return false;
        }

        Map<String, Integer> studentCoursesMap = studentsMap.get(student);
        if (!studentCoursesMap.containsKey(course)) {
            return false;
        }

        studentCoursesMap.put(course, studentCoursesMap.get(course) + inc);
        studentsMap.remove(student);
        studentsMap.put(student, studentCoursesMap);
        return true;
    }
}