/**
 * Written by Sterling Zerr 9/20/16
 * This is the main class which takes 5 arguments:
 * 1.) Operation to perform:
 * a.) conceal - steganogrifies a message into the image
 * b.) reveal  - reveals the steganogrified message in image
 * 2.) Flags of which color bytes to conceal the message into (-rgb)
 * a.) 'rgb'
 * b.) 'rg'
 * c.) 'rb'
 * d.) 'bg'
 * e.) 'r'
 * f.) 'b'
 * g.) 'g'
 * 3.) Path of the input image to conceal the message into
 * 4.) Path of the output image which holds the concealed message
 * 5.) Message to conceal into the image
 */

public class Steganography {

    public static void main(String[] args) {

        String operation = null;
        String flags = null;
        String inputFilePath = null;

        if (args.length > 0 && !args[0].equals("help")) {
            if (args[0].equals("conceal") || args[0].equals("reveal")) {
                operation = args[0];
            } else {
                System.out.println("Error: invalid operation - use 'conceal' or 'reveal'");
                System.exit(0);
            }
        } else {
            System.out.println("\nUsage: \n" +
                    "    conceal <flags> <inputPath> <outputPath> <message>\n" +
                    "    reveal <flags> <inputPath>\n\n" +
                    "    operation: 'conceal' or 'reveal'\n" +
                    "    flags: '-rgb' or '-rg' or '-rb' or '-bg' or '-r' or '-b' or '-g'\n" +
                    "    inputPath: path of the input image to coneal the message into\n" +
                    "    outputPath: path of the output image which holds the concealed message\n" +
                    "    message: message to conceal into the image");
            System.exit(0);
        }

        // TODO - regex checking for valid flags
        flags = args[1];

        if (args.length == 3) {
            inputFilePath = args[2];
        }
        if (args.length == 5) {
            inputFilePath = args[2];
        }
        if (args.length > 5) {
            System.out.println("Error: invalid number of arguments");
            System.exit(0);
        }

        if (operation.equals("conceal")) {
            if (args.length > 4 && args[3] != null && args[4] != null) {
                String outputFilePath = args[3];
                String message = args[4];

                StegImage si = new StegImage(inputFilePath, outputFilePath);
                Steganogrifier steganogrifier = new Steganogrifier(si);
                steganogrifier.selectableSteganographi(message, flags);
                String decodedMessage = steganogrifier.selectableDesteganographi(flags);

                if (message.equals(decodedMessage)) {
                    si.saveImage();
                } else {
                    System.out.println("There was an error concealing the messge");
                    System.out.println(message + "   !=   " + decodedMessage);
                }

            } else {
                System.out.println("Error: invalid number of arguments");
                System.exit(0);
            }


        } else if (operation.equals("reveal")) {
            StegImage si = new StegImage(inputFilePath);
            Steganogrifier steganogrifier = new Steganogrifier(si);
            String decodedMessage = steganogrifier.selectableDesteganographi(flags);
            System.out.println(decodedMessage);
        }
    }
}
