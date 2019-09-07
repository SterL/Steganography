/**
 * This is the main class which takes 3 or 5 arguments depending on the
 * operation:
 *
 * 1.) Operation to perform:
 * a.) conceal - steganografies a message into the
 * image
 * b.) revseal  - reveals the steganografied message in image
 *
 * 2.) Flags of which color bytes to conceal the message into (-rgb)
 * a.) '-rgb'
 * b.) '-rg'
 * c.) '-rb'
 * d.) '-bg'
 * e.) '-r'
 * f.) '-b'
 * g.) '-g'
 *
 * 3.) Path of the input image to conceal the message into
 * 4.) Path of the output image which holds the concealed message
 * 5.) Message to conceal into the image
 */
public class Steganography {

  public static void main(String[] args) {

    // if these conditions aren't met, show usage
    if (args.length != 0 && args.length > 2 &&
      (args[0].equals("conceal") || args[0].equals("reveal"))) {
      // TODO - regex checking for valid flags
      String flags = args[1];
      String inputFilePath = args[2];

      if (args[0].equals("conceal")) {
        if (args.length == 5) {
          String outputFilePath = args[3];
          String message = args[4];

          StegImage si = new StegImage(inputFilePath, outputFilePath);
          Steganografier Steganografier = new Steganografier(si);
          Steganografier.selectableSteganographi(message, flags);
          String decodedMessage = Steganografier.selectableDesteganographi(flags);

          // before saving the image, this checks to make sure the
          // encoded message is the same as the decoded message
          if (message.equals(decodedMessage)) {
            si.saveImage();
          } else {
            System.out.println("There was an error concealing the messge");
            System.out.println(message + "   !=   " + decodedMessage);
          }

        } else {
          System.out.println("Error: invalid number of arguments for conceal");
          System.exit(0);
        }

      } else if (args[0].equals("reveal")) {
        if (args.length == 3) {
          StegImage si = new StegImage(inputFilePath);
          Steganografier Steganografier = new Steganografier(si);
          String decodedMessage = Steganografier.selectableDesteganographi(flags);
          System.out.println(decodedMessage);
        } else {
          System.out.println("Error: invalid number of arguments for reveal");
          System.exit(0);
        }
      }

    } else {
      System.out.println("\nUsage: \n" +
        "    conceal <flags> <inputPath> <outputPath> <message>\n" +
        "    reveal <flags> <inputPath>\n\n" +
        "    flags: '-rgb' or '-rg' or '-rb' or '-bg' or '-r' or '-b' or '-g'\n" +
        "    inputPath: path of the input image to coneal the message into\n" +
        "    outputPath: path of the output image which holds the concealed message\n" +
        "    message: \"message\" to conceal into the image");
      System.exit(0);
    }
  }
}
