import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;

public class LinkExecutor extends RecursiveTask<String> {
    private String url;
    private static CopyOnWriteArraySet<String> allLink = new CopyOnWriteArraySet<>();
    private static final int SLASH = 3;
    public static File outputFile;

    public LinkExecutor(String url) {
        this.url = url.trim();
    }

    @Override
    protected String compute() {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<LinkExecutor> task = new HashSet<>();

        getChildren(task);

        for (LinkExecutor linkExecutor : task) {
            stringBuffer.append(linkExecutor.join());
        }
        return stringBuffer.toString();
    }

    private void getChildren(Set<LinkExecutor> task) {
        Document document;
        Elements elements;
        try {
            Thread.sleep(150);
            document = Jsoup.connect(url).get();
            elements = document.select("a");
            for (Element element : elements) {
                String attr = element.attr("abs:href");
                if (!attr.isEmpty() && attr.matches(url + ".*") && !allLink.contains(attr) && !attr.contains("#") && !attr.contains("?")) {
                    LinkExecutor linkExecutor = new LinkExecutor(attr);
                    linkExecutor.fork();
                    task.add(linkExecutor);
                    allLink.add(attr);
                }
                List<String> sortedList = new ArrayList<>(allLink);
                Collections.sort(sortedList);
                FileWriter writer = new FileWriter(outputFile, false);
                String tab = "";
                writer.write(url + "\n");
                for(String s: sortedList)
                {
                    for (int i = 0; i < s.split("/").length - SLASH;i++){
                        tab = tab + "\t";
                    }
                    tab = tab + s + "\n";
                    writer.write(tab);
                    tab = "";
                }
                writer.flush();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}