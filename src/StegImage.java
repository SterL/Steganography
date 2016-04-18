/**
 * Written by Sterling Zerr 9/20/16
 *
 * This class handles reading and writing of image files. You can pass the path
 * of an images directory, imagefilename, outputfilename to the constructor.
 * This will allow you to retrieve the byte array of the image file. You can
 * then alter the images RGB values from the byte array, and then save the newly
 * altered pixels into a new image.
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class StegImage {

    private BufferedImage inputImage;
    private BufferedImage outputImage;
    private File inputFile;
    private File outputFile;
    private String path;
    private String inputFileName;
    private String outputFileName;
    private String fileType;
    private int height;
    private int width;

    /**
     * Constructor that takes a path to an images dir, inputFileName, and
     * outputFileName. This saves the encoded image to a new, near-duplicate
     * image instead of overwriting the original.
     *
     * @param path
     * @param inputFileName
     * @param outputFileName
     */
    public StegImage(String path, String outputFileName) {
        try {
            this.path = path;
            this.inputFileName = extractFileName(path);
            this.outputFileName = outputFileName;
            this.fileType = path.substring(path.length() - 4, path.length());
            this.inputFile = new File(path);
            this.outputFile = new File(separateOutputPath(path, outputFileName));
            this.inputImage = ImageIO.read(inputFile);
            this.height = inputImage.getHeight();
            this.width = inputImage.getWidth();
            this.outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // FOR BMP FILES

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Constructor that just takes a path to an image file. From here the user
     * can get the byte array from an image and the user can also potentially
     * overwrite the current image.
     *
     * @param path
     */
    public StegImage(String path) {
        try {
            this.path = path;
            this.fileType = path.substring(path.length() - 4, path.length());
            this.inputFile = new File(path);
            this.outputFile = new File(path);
            this.inputImage = ImageIO.read(inputFile);
            this.height = inputImage.getHeight();
            this.width = inputImage.getWidth();
            this.outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // FOR BMP FILES

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * This method returns the byte array in an int[] of the image. From this
     * data, you can retrieve the RGB values of each pixel.
     *
     * @return
     */
    public int[] getByteArray() {
        return inputImage.getRGB(0, 0, width, height, null, 0, width);
    }

    /**
     * This method saves a byte array (newPixels) to the image container
     * specified as outputFileName. The image will be saved to the same dir as
     * the input image.
     *
     * @param newPixels
     * @return
     */
    public boolean saveImage(int[] newPixels) {
        try {
            outputImage.setRGB(0, 0, width, height, newPixels, 0, width);
            if (ImageIO.write(outputImage, "bmp", outputFile) == true) {
                System.out.println("The Steganogrified image has been saved successfully to: " + separateOutputPath(path, outputFileName));
                return true;
            } else {
                System.out.println("Error: The file was not saved successfully");
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(StegImage.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This method returns the filename from the provided path
     *
     * @param path
     * @return
     */
    private String extractFileName(String path) {
        String[] strings = path.split("\\\\");
        return strings[strings.length - 1];
    }

    /**
     * This method replaces the inputFileName with the outputFileName in the
     * path and returns the path of the output file.
     *
     * @param path
     * @param outputFileName
     * @return
     */
    private String separateOutputPath(String path, String outputFileName) {
        String s = "";
        String[] strings = path.split("\\\\");
        for (int i = 0; i < strings.length - 1; i++) {
            if (i == 0) {
                s = strings[i];
            } else {
                s = s + "\\" + strings[i];
            }
        }
        return s + "\\" + outputFileName;
    }
}
