/**
 * Written by Sterling Zerr 9/20/16
 *
 * This is the main class which takes 4 arguments: 1.) Flags of which color
 * bytes to encode message into (-rgb) 1.) Input image path and filename 2.)
 * Output image name (saves to same path as input) 3.) Message to encode
 */
package steganography;

public class Steganography {

    public static void main(String[] args) {

        if (args[0] != null && args[1] != null && args[2] != null) {
            String flags = args[0];
            String fileDir = args[1];
            String outputFile = args[2];
            String message = args[3];

            StegImage si = new StegImage(fileDir, outputFile);
            Steganogrifier steganogrifier = new Steganogrifier(si.getByteArray());
            int[] encodedPixels = steganogrifier.selectableSteganographi(message, flags);
            String decryptedMessage = steganogrifier.selectableDesteganographi(encodedPixels, flags);

            if (message.equals(decryptedMessage)) {
                si.saveImage(encodedPixels);
                System.out.println(message + "   ==   " + decryptedMessage);
                System.out.println("File Encoded");
            } else {
                System.out.println("There was an error encoding the messge");
                System.out.println(message + "   !=   " + decryptedMessage);
            }

        } else {
            System.out.print("Error: args empty");
            System.exit(0);
        }

    }
}
