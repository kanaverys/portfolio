import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args){
        try {
            System.out.println("Введите адрес целевого вебсайта с указанием протокола: ");
            Scanner scanner = new Scanner(System.in);
            String url = scanner.nextLine();
            System.out.println("Введите путь к директории, куда должна быть сохранена карта сайта: ");
            String outputPath = scanner.nextLine();
            LinkExecutor.outputFile = new File(outputPath + "/map.txt");
            LinkExecutor.outputFile.createNewFile();
            LinkExecutor executor = new LinkExecutor(url);
            new ForkJoinPool().invoke(executor);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
