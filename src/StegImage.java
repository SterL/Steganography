/**
 * Written by Sterling Zerr 9/20/16
 *
 * This class handles reading and writing of image files. You can pass the path
 * of an image and the path of the output image to the constructor. This will
 * allow you to retrieve the byte array of the image file. You can then alter
 * the images RGB values from the byte array, and then save the newly altered
 * pixels into a new image.
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
  private String inputFilePath;
  private String outputFilePath;
  private String fileType;
  private int height;
  private int width;
  private int[] pixels;

  /**
   * Constructor that takes a path to an input image, and a path to an output
   * image. This saves the concealed image to a new, near-duplicate image
   * instead of overwriting the original.
   *
   * @param path
   * @param inputFileName
   * @param outputFileName
   */
  public StegImage(String inputFilePath, String outputFilePath) {
    try {
      this.inputFilePath = extractFileName(inputFilePath);
      this.outputFilePath = extractFileName(outputFilePath);
      this.fileType = inputFilePath.substring(inputFilePath.length() - 4, inputFilePath.length());
      this.inputFile = new File(inputFilePath);
      this.outputFile = new File(outputFilePath);
      this.inputImage = ImageIO.read(inputFile);
      this.height = inputImage.getHeight();
      this.width = inputImage.getWidth();
      this.outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // FOR BMP FILES
      this.pixels = getByteArray();

    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  /**
   * Constructor that takes a path to an input image. This saves the encoded
   * image to a new, near-duplicate image instead of overwriting the original.
   *
   * @param inputFileName
   */
  public StegImage(String inputFilePath) {
    try {
      this.inputFilePath = extractFileName(inputFilePath);
      this.fileType = inputFilePath.substring(inputFilePath.length() - 4, inputFilePath.length());
      this.inputFile = new File(inputFilePath);
      this.inputImage = ImageIO.read(inputFile);
      this.height = inputImage.getHeight();
      this.width = inputImage.getWidth();
      //this.outputImage = new BufferedImage(width, height, BufferedImage
      // .TYPE_INT_RGB); // FOR BMP FILES
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
   * This method saves a byte array (newPixels) to the image container specified
   * as outputFileName. The image will be saved to the same dir as the input
   * image.
   *
   * @param newPixels
   *
   * @return
   */
  public boolean saveImage() {
    try {
      outputImage.setRGB(0, 0, width, height, this.pixels, 0, width);
      if (ImageIO.write(outputImage, "bmp", outputFile) == true) {
        System.out.println("The Steganografied image has been saved successfully to: " + this.outputFile.getAbsolutePath());//outputFilePath);
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

  public void setPixels(int[] pixels) {
    this.pixels = pixels;
  }

  /**
   * This method returns the filename from the provided path
   *
   * @param path
   *
   * @return
   */
  private String extractFileName(String path) {
    String[] strings = path.split("\\\\");
    return strings[strings.length - 1];
  }

}
