import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        System.out.println("Введите путь к целевому изображению: ");
        Scanner scanner = new Scanner(System.in);
        File srcFile = new File(scanner.nextLine());
        System.out.println("Введите цирину и высоту целевого изображения через пробел: ");
        String[] sizes = scanner.nextLine().split(" ");
        System.out.println("Введите путь к директории, куда будет сохранено новое изображение: ");
        String dstPath = scanner.nextLine();

        ImageResizer resizer = new ImageResizer(srcFile, dstPath, Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
        resizer.start();
    }
}