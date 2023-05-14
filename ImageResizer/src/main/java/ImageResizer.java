import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageResizer extends Thread {
    int newWidth;
    int newHeight;
    String dstPath;
    File srcFile;

    @Override
    public void run() {
        try{
            BufferedImage newImage = Scalr.resize(ImageIO.read(this.srcFile), Scalr.Mode.FIT_EXACT, this.newWidth, this.newHeight);
            File newFile = new File(this.dstPath + "/" + this.srcFile.getName());
            ImageIO.write(newImage, "jpg", newFile);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ImageResizer(File srcFile, String dstPath, int newWidth, int newHeight) {
        this.srcFile = srcFile;
        this.dstPath = dstPath;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
}