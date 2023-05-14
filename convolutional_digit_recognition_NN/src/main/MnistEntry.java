package main;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;


public class MnistEntry
{
    private final int index;
    private final byte label;
    private final int numRows;
    private final int numCols;
    private final byte[] imageData;

    MnistEntry(int index, byte label, int numRows, int numCols,
               byte[] imageData)
    {
        this.index = index;
        this.label = label;
        this.numRows = numRows;
        this.numCols = numCols;
        this.imageData = imageData;
    }

    public int getIndex()
    {
        return index;
    }

    public byte getLabel()
    {
        return label;
    }

    public int getNumRows()
    {
        return numRows;
    }

    public int getNumCols()
    {
        return numCols;
    }


    public byte[] getImageData()
    {
        return imageData;
    }

    public BufferedImage createImage()
    {
        BufferedImage image = new BufferedImage(getNumCols(),
                getNumRows(), BufferedImage.TYPE_BYTE_GRAY);
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        DataBufferByte dataBufferByte = (DataBufferByte) dataBuffer;
        byte data[] = dataBufferByte.getData();
        System.arraycopy(getImageData(), 0, data, 0, data.length);
        return image;
    }


    @Override
    public String toString()
    {
        String indexString = String.format("%05d", index);
        return "classes.MnistEntry["
                + "index=" + indexString + ","
                + "label=" + label + "]";
    }

}