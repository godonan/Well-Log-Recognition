package louisiana.Classes;

import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scan {
    private File referencePosImageFile;
    private int imageWidth;
    private int imageHeight;

    private double scanRelativeWidth;
    private double scanRelativeHeight;
    private double scanRelativeX1;
    private double scanRelativeY1;
    private double scanRelativeX2;
    private double scanRelativeY2;

    public Scan(File referencePosImageFile) throws IOException {
        this.referencePosImageFile = referencePosImageFile;
        try {
            BufferedImage bimg = ImageIO.read(referencePosImageFile);
            this.imageWidth = bimg.getWidth();
            this.imageHeight = bimg.getHeight();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Scan(File referencePosImageFile, int width, int height){
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public Scan(int width, int height){
        this.imageWidth = width;
        this.imageHeight = height;
    }

    /**
     * Generates scan relative dimensions from a bounding box
     * @param boundingBox
     */
    public void addScan(int[] boundingBox){
        // Bounding box has format (x,y) in [top-right, top-left, bottom-left, bottom-right]
        scanRelativeX1 = Math.min(boundingBox[0], boundingBox[6]) / (double) imageWidth;
        scanRelativeY1 =  Math.min(boundingBox[1], boundingBox[7]) / (double) imageHeight;
        scanRelativeX2 = Math.max(boundingBox[2], boundingBox[4]) / (double) imageWidth;
        scanRelativeY2 = Math.max(boundingBox[3], boundingBox[5]) / (double) imageHeight;
        scanRelativeWidth = Math.abs(scanRelativeX1 - scanRelativeX2);
        scanRelativeHeight = Math.abs(scanRelativeY1 - scanRelativeY2);
    }

    /**
     * Generates scan relative dimensions from a geometric Rectangle
     * @param rectangle
     */
    public void addScan(Rectangle rectangle){
        scanRelativeX1 = rectangle.getX() / (double) imageWidth;
        scanRelativeY1 = rectangle.getY() / (double) imageHeight;
        scanRelativeX2 = (rectangle.getX() + rectangle.getWidth()) / (double) imageWidth;
        scanRelativeY2 = (rectangle.getY() + rectangle.getHeight()) / (double) imageHeight;
        scanRelativeWidth = Math.abs(scanRelativeX1 - scanRelativeX2);
        scanRelativeHeight = Math.abs(scanRelativeY1 - scanRelativeY2);
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * Returns percentage results of difference between scans
     * @param scan
     * @return box with differences between the X1,Y1,X2,Y2 dimensions
     */
    public double[] compareTo(Scan scan){
        double[] arr = { 100 * Math.abs(scan.scanRelativeX1 - this.scanRelativeX1),
                100 * Math.abs(scan.scanRelativeY1 - this.scanRelativeY1),
                100 * Math.abs(scan.scanRelativeX2 - this.scanRelativeX2),
                100 * Math.abs(scan.scanRelativeY2 - this.scanRelativeY2)
        };
        return arr;
    }

    /**
     * Creates a rectangle object relative to given image width and height
     * @param imgWidth
     * @param imgHeight
     * @return
     */
    public Rectangle getRectangle(int imgWidth, int imgHeight){
         int X = (int) Math.floor(scanRelativeX1 * imgWidth);
         int Y = (int) Math.floor(scanRelativeY1 * imgHeight);
         int width = (int) Math.ceil(scanRelativeWidth * imgWidth);
         int height = (int) Math.ceil(scanRelativeHeight * imgHeight);
         return new Rectangle(X, Y, width, height);
    }

    @Override
    public String toString(){
        return String.format("X1: %f, Y1: %f, X2: %f, Y2: %f",
                scanRelativeX1, scanRelativeY1, scanRelativeX2, scanRelativeY2);
    }
}
