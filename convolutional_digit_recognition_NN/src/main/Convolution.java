package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class Convolution {

    public static int[][] ATMConversion(byte[] array, int height){
        int width = array.length / height;
        int[][] matrix = new int[width][height];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = array[height * i + j];
            }
        }

        return matrix;
    }

    public static int[][] maxPooling(int[][] matrix, int poolSize){
        int OMDY = matrix.length / poolSize;
        int OMDX = matrix[0].length / poolSize;
        int[][] output = new int[OMDY][OMDX];
        int OII = 0;
        int OIJ = 0;

        for (int i = 0; i < matrix.length; i += poolSize) {
            for (int j = 0; j < matrix[0].length; j += poolSize) {
                int[][] subMatrix = new int[poolSize][poolSize];
                for (byte k = 0; k < poolSize; k++) {
                    for (byte f = 0; f < poolSize; f++) {
                        subMatrix[k][f] = matrix[i + k][j + f];
                    }
                }
                int[] maxValues = new int[poolSize];
                for (int q = 0; q < subMatrix.length; q++) {
                    maxValues[q] = Arrays.stream(subMatrix[q]).max().getAsInt();
                }
                output[OII][OIJ] = Arrays.stream(maxValues).max().getAsInt();
                if(OIJ == OMDX - 1){
                    OII++;
                    OIJ = 0;
                    continue;
                }
                OIJ++;
            }
        }

        return output;
    }

    public static int[][] singleKernelMulticolorConvolution(int[][][] matrix, int[][] kernel){
        int CMDY = matrix[0].length - kernel.length + 1;
        int CMDX = matrix[0][0].length - kernel[0].length + 1;

        int[][] redMatrix = monochromeConvolution(matrix[0], kernel);
        int[][] greenMatrix = monochromeConvolution(matrix[1], kernel);
        int[][] blueMatrix = monochromeConvolution(matrix[2], kernel);

        int[][] output = new int[CMDY][CMDX];

        for (int y = 0; y < CMDY; y++) {
            for (int x = 0; x < CMDX; x++) {
                output[y][x] = redMatrix[y][x] + greenMatrix[y][x] + blueMatrix[y][x];
            }
        }

        return output;
    }

    public static int[][] monochromeConvolution(int[][] matrix, int[][] filter){
        int CMDY = matrix.length - filter.length + 1;
        int CMDX = matrix[0].length - filter[0].length + 1;
        int[][] output = new int[CMDY][CMDX];

        for (int my = 0; my < CMDY; my++) {
            for (int mx = 0; mx < CMDX; mx++) {
                int yShift = 0;
                int xShift = 0;

                int sum = 0;
                for (int fy = 0; fy < filter.length; fy++) {
                    for (int fx = 0; fx < filter[0].length; fx++) {
                        sum += matrix[my + yShift][mx + xShift] * filter[fy][fx];
                        xShift += 1;
                        if(xShift == filter[0].length){
                            yShift += 1;
                            xShift = 0;
                        }
                    }
                }
                output[my][mx] = sum;
            }
        }
        return output;
    }

    public static int[][] multicolorConvolution(int[][][] matrix, int[][] vFilter, int[][] hFilter){
        int CMDY = matrix[0].length - hFilter.length + 1;
        int CMDX = matrix[0][0].length - vFilter[0].length + 1;

        int[][] redMatrixV = monochromeConvolution(matrix[0], vFilter);
        int[][] greenMatrixV = monochromeConvolution(matrix[1], vFilter);
        int[][] blueMatrixV = monochromeConvolution(matrix[2], vFilter);

        int[][] redMatrixH = monochromeConvolution(matrix[0], hFilter);
        int[][] greenMatrixH = monochromeConvolution(matrix[1], hFilter);
        int[][] blueMatrixH = monochromeConvolution(matrix[2], hFilter);

        int[][] output = new int[CMDY][CMDX];

        for (int y = 0; y < CMDY; y++) {
            for (int x = 0; x < CMDX; x++) {
                output[y][x] = redMatrixV[y][x] + greenMatrixV[y][x] + blueMatrixV[y][x];
                output[y][x] += redMatrixH[y][x] + greenMatrixH[y][x] + blueMatrixH[y][x];

//                output[y][x] = output[y][x] / 2295 * 255;
            }
        }

        return output;
    }

    public static void MTIConversion(int[][] edges, int maxConvolutedPixelValue){
        try {
            BufferedImage image = new BufferedImage(edges.length, edges[0].length, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i < edges.length; i++) {
                for(int j = 0; j < edges[0].length; j++) {
                    int a = normalize(edges[i][j], maxConvolutedPixelValue);
                    Color newColor = new Color(a, a, a);
                    image.setRGB(i ,j, newColor.getRGB());
                }
            }
            File output = new File("GreyScale.jpg");
            ImageIO.write(image, "jpg", output);
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int[][][] ITMConversion(String path){
        int[][][] output = null;
        try{
            BufferedImage image = ImageIO.read(new File(path));
            output = new int[3][image.getWidth()][image.getHeight()];
            int[][] redMatrix = new int[image.getWidth()][image.getHeight()];
            int[][] greenMatrix = new int[image.getWidth()][image.getHeight()];
            int[][] blueMatrix = new int[image.getWidth()][image.getHeight()];
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color color = new Color(image.getRGB(i, j));
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    redMatrix[i][j] = red;
                    greenMatrix[i][j] = green;
                    blueMatrix[i][j] = blue;
                }
            }
            output[0] = redMatrix;
            output[1] = greenMatrix;
            output[2] = blueMatrix;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return output;
    }

    public static int[][][] ITMConversion(BufferedImage image){
        int[][][] output = null;
        try{
            output = new int[3][image.getWidth()][image.getHeight()];
            int[][] redMatrix = new int[image.getWidth()][image.getHeight()];
            int[][] greenMatrix = new int[image.getWidth()][image.getHeight()];
            int[][] blueMatrix = new int[image.getWidth()][image.getHeight()];
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color color = new Color(image.getRGB(i, j));
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    redMatrix[i][j] = red;
                    greenMatrix[i][j] = green;
                    blueMatrix[i][j] = blue;
                }
            }
            output[0] = redMatrix;
            output[1] = greenMatrix;
            output[2] = blueMatrix;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return output;
    }

    public static int normalize(int val, int max){
        return (int)((double) val) * 255 / max;
    }
}