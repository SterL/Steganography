/**
 * Written by Sterling Zerr 9/20/16 Updated 4/17/16
 *
 * This class handles the byte manipulation of image files to encode hidden
 * messages into the lowest order bit of the red, green, and/or blue bytes from
 * the pixel byte array. This class can also decode the message, and clear the
 * message from the image.
 *
 */
package steganography;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Steganogrifier {

    //This int/byte array stores the pixel data we need to encode a message into 
    private int[] inputImageByteArray;

    public Steganogrifier(int[] inputImageByteArray) {
        this.inputImageByteArray = inputImageByteArray;
    }

    /**
     * This method is the heart of the application. It takes the message to
     * encode and then converts the message to a byte array of the ASCII values
     * in that message. It then iterates over every byte (letter) in the array,
     * again inner-iterating over every bit in those bytes. If the bit is equal
     * to 1, we increment the red, green, or blue pixel value by 1 to change the
     * lowest order bit. Else if the bit being read is 0, we don't change any
     * red, green, or blue bytes because 0 already exists in the lowest bit in
     * those bytes. Each bit of the message is stored sequentially in the R, G,
     * B values respectively. Since one letter is one byte (8 bits), one letter
     * will fit in three pixels (9 bytes -- 1 bit in each red, green, blue byte)
     * and there will be a leftover bit in the unused blue byte of every third
     * pixel.
     *
     * @param message
     * @return
     */
    public int[] steganographi(String message) {
        inputImageByteArray = clearLowestBit();
        int byteCounter = 0;
        byte[] asciiArray = message.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < asciiArray.length; i++) {
            String letterAsciiBinary = insertPadding(asciiArray[i]);
            for (int k = 0; k < letterAsciiBinary.length(); k++) {
                Color c = new Color(inputImageByteArray[byteCounter]);
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                    if (k % 3 == 0) {
                        inputImageByteArray[byteCounter] = addABit(r, g, b, "r");
                    } else if (k % 3 == 1) {
                        inputImageByteArray[byteCounter] = addABit(r, g, b, "g");
                        if (k == 7) {
                            byteCounter++;
                        }
                    } else if (k % 3 == 2) {
                        inputImageByteArray[byteCounter] = addABit(r, g, b, "b");
                        byteCounter++;
                    }
                } else {
                    if (k % 3 == 2 || k == 7) {
                        byteCounter++;
                    }
                }
            }
        }
        System.out.println("Encoding " + asciiArray.length + " letters ("
                + asciiArray.length + " bytes" + ") into " + byteCounter
                + " pixels (" + byteCounter * 3 + " bytes)");
        return inputImageByteArray;
    }

    /**
     * This method is a secondary to steganographi. It takes the message to
     * encode and flags to to tell it which colored bytes to hide the image in.
     * It then converts the message to a byte array of the ASCII values in that
     * message. It then iterates over every byte (letter) in the array, again
     * inner-iterating over every bit in those bytes. If the bit is equal to 1,
     * we increment the flagged colors pixels value by 1 to change the lowest
     * order bit. Else if the bit being read is 0, we don't change any flagged
     * colors bytes because 0 already exists in the lowest bit in those bytes.
     * Each bit of the message is stored sequentially in the R, G, B values
     * respectively. Since one letter is one byte (8 bits), one letter will fit
     * in three pixels (9 bytes -- 1 bit in each red, green, blue byte) and
     * there will be a leftover bit in the blue byte of every third pixel.
     *
     * @param message - The message that will be encoded into the image.
     * @param flags - Flags that will encode in a specific way.
     *
     * -rgb : encodes using the normal method of using all 3 colors in each
     * pixel to save max amount of space to allow for a larger message. This
     * includes any permutation of -rgb (-bgr, -grb, rbg). (Code 0).
     *
     * -rg : encodes the message into the red and green pixels only. This
     * includes any permutation of -rg (-gr, -RG, -GR). (Code 1).
     *
     * -rb : encodes the message into the red and blue pixels only. This
     * includes any permutation of -rb (-br, -BR, -RB). (Code 2).
     *
     * -gb : encodes the message into the green and blue pixels only. This
     * includes any permutation of -gb (-bg, -GB, -BG). (Code 3).
     *
     * -r : encodes the message into the red pixels only. (Code 4).
     *
     * -g : encodes the message into the green pixels only. (Code 5).
     *
     * -b : encodes the message into the blue pixels only. (Code 6).
     *
     *
     * @return
     */
    public int[] selectableSteganographi(String message, String flags) {
        int flagCode = verifyFlags(flags);
        if (flagCode == 0) {
            return steganographi(message);
        } else {
            inputImageByteArray = clearLowestBit();
            int byteCounter = 0;
            byte[] asciiArray = message.getBytes(StandardCharsets.US_ASCII);

            for (int i = 0; i < asciiArray.length; i++) {
                String letterAsciiBinary = insertPadding(asciiArray[i]);
                for (int k = 0; k < letterAsciiBinary.length(); k++) {
                    Color c = new Color(inputImageByteArray[byteCounter]);
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    if (flagCode == 1) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            if (k % 2 == 0) {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "r");
                            } else {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "g");
                                byteCounter++;
                            }
                        } else {
                            if (k % 2 == 1) {
                                byteCounter++;
                            }
                        }
                    } else if (flagCode == 2) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            if (k % 2 == 0) {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "r");
                            } else {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "b");
                                byteCounter++;
                            }
                        } else {
                            if (k % 2 == 1) {
                                byteCounter++;
                            }
                        }
                    } else if (flagCode == 3) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            if (k % 2 == 0) {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "g");
                            } else {
                                inputImageByteArray[byteCounter] = addABit(r, g, b, "b");
                                byteCounter++;
                            }
                        } else {
                            if (k % 2 == 1) {
                                byteCounter++;
                            }
                        }
                    } else if (flagCode == 4) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            inputImageByteArray[byteCounter] = addABit(r, g, b, "r");
                        }
                        byteCounter++;
                    } else if (flagCode == 5) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            inputImageByteArray[byteCounter] = addABit(r, g, b, "g");
                        }
                        byteCounter++;
                    } else if (flagCode == 6) {
                        if (letterAsciiBinary.substring(k, k + 1).equals("1")) {
                            inputImageByteArray[byteCounter] = addABit(r, g, b, "b");
                        }
                        byteCounter++;
                    }
                }
            }

            System.out.println("Encoding " + asciiArray.length + " letters ("
                    + asciiArray.length + " bytes" + ") into " + byteCounter
                    + " pixels (" + byteCounter * 3 + " bytes)");

        }
        return inputImageByteArray;
    }

    /**
     * This method decodes the message from the int[] pixels that you feed it
     * and it returns the message as a string.
     *
     * @param pixels
     * @return
     */
    public String desteganographi(int[] pixels) {
        ArrayList messageBytes = new ArrayList();
        String decodedString = "";
        for (int i = 0; i < pixels.length; i++) {
            Color c = new Color(pixels[i]);
            String r = Integer.toBinaryString(c.getRed());
            String g = Integer.toBinaryString(c.getGreen());
            String b = Integer.toBinaryString(c.getBlue());
            messageBytes.add(r.substring(r.length() - 1, r.length()));
            messageBytes.add(g.substring(g.length() - 1, g.length()));
            messageBytes.add(b.substring(b.length() - 1, b.length()));

            //Break the loop if the message has been fully read.
            if (i > 8) {
                if (messageBytes.get(i - 7).equals("0")
                        && messageBytes.get(i - 6).equals("0")
                        && messageBytes.get(i - 5).equals("0")
                        && messageBytes.get(i - 4).equals("0")
                        && messageBytes.get(i - 3).equals("0")
                        && messageBytes.get(i - 2).equals("0")
                        && messageBytes.get(i - 1).equals("0")
                        && messageBytes.get(i).equals("0")) {
                    break;

                }
            }
        }

        //Crack the code :)
        for (int i = 0; i < messageBytes.size(); i = i + 9) {
            String ss = messageBytes.get(i).toString();
            for (int k = 1; k < 8; k++) {
                ss = ss + messageBytes.get(i + k).toString();
            }
            if (ss.equals("00000000")) {
                break; //if we read this byte, we are at the end of the message.
            }
            int asciiCode = Integer.parseInt(ss, 2);
            decodedString = decodedString + Character.toString((char) asciiCode);
        }
        return decodedString;
    }

    /**
     * This method decodes the message from the int[] pixels that you feed it
     * and it returns the message as a string.
     *
     * @param pixels
     * @param flags
     * @return
     */
    public String selectableDesteganographi(int[] pixels, String flags) {
        int flagCode = verifyFlags(flags);
        if (flagCode == 0) {
            return desteganographi(pixels);
        } else {
            ArrayList messageBytes = new ArrayList();
            String decodedString = "";
            for (int i = 0; i < pixels.length; i++) {
                Color c = new Color(pixels[i]);
                String r = Integer.toBinaryString(c.getRed());
                String g = Integer.toBinaryString(c.getGreen());
                String b = Integer.toBinaryString(c.getBlue());

                if (flagCode == 1) {
                    messageBytes.add(r.substring(r.length() - 1, r.length()));
                    messageBytes.add(g.substring(g.length() - 1, g.length()));
                } else if (flagCode == 2) {
                    messageBytes.add(r.substring(r.length() - 1, r.length()));
                    messageBytes.add(b.substring(b.length() - 1, b.length()));
                } else if (flagCode == 3) {
                    messageBytes.add(g.substring(g.length() - 1, g.length()));
                    messageBytes.add(b.substring(b.length() - 1, b.length()));
                } else if (flagCode == 4) {
                    messageBytes.add(r.substring(r.length() - 1, r.length()));
                } else if (flagCode == 5) {
                    messageBytes.add(g.substring(g.length() - 1, g.length()));
                } else if (flagCode == 6) {
                    messageBytes.add(b.substring(b.length() - 1, b.length()));
                }

                //Break the loop if the message has been fully read.
                if (i > 8) {
                    if (messageBytes.get(i - 7).equals("0")
                            && messageBytes.get(i - 6).equals("0")
                            && messageBytes.get(i - 5).equals("0")
                            && messageBytes.get(i - 4).equals("0")
                            && messageBytes.get(i - 3).equals("0")
                            && messageBytes.get(i - 2).equals("0")
                            && messageBytes.get(i - 1).equals("0")
                            && messageBytes.get(i).equals("0")) {
                        break;

                    }
                }
            }

            //Crack the code :)
            for (int i = 0; i < messageBytes.size(); i = i + 8) {
                String ss = messageBytes.get(i).toString();
                for (int k = 1; k < 8; k++) {
                    ss = ss + messageBytes.get(i + k).toString();
                }
                if (ss.equals("00000000")) {
                    break; //if we read this byte, we are at the end of the message.
                }
                int asciiCode = Integer.parseInt(ss, 2);
                decodedString = decodedString + Character.toString((char) asciiCode);
            }
            return decodedString;
        }

    }

    /**
     * This method is awesome because it uses the bitwise & operation against
     * 254 (or 11111110 in binary) to clear every low order bit setting it to 0.
     * Because we are only clearing/using the lowest order bit, the image
     * appears to be unaffected though the RGB values are in fact likely 1 value
     * lower than what it probably should be.
     *
     * @return
     */
    public int[] clearLowestBit() {
        for (int i = 0; i < inputImageByteArray.length; i++) {
            Color c = new Color(inputImageByteArray[i]);
            int r = c.getRed() & 254; // 254 = 11111110
            int g = c.getGreen() & 254;
            int b = c.getBlue() & 254;
            int rgb = getRGB(r, g, b);
            inputImageByteArray[i] = rgb;
        }
        return inputImageByteArray;
    }

    /**
     * This method takes a red byte, green byte, and a blue byte and fixes those
     * bytes together to form the 32 bit RGB bytes which are stored into our
     * pixel arrays.
     *
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private int getRGB(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }

    /**
     * This is kind of a dumb method and it probably should be handled in a more
     * conventional way, however, it works for what I need it for. This method
     * inserts the proper amount of 0's to the beginning of the byte string if
     * that particular byte string does not have enough 0's. We need to do this
     * because we are reading and writing binary by the byte (8 bits, in case
     * you didn't know)
     *
     * @param asciiKey
     * @return
     */
    private String insertPadding(int asciiKey) {
        String letterAsciiBinary = Integer.toBinaryString(asciiKey);
        if (letterAsciiBinary.length() == 7) {
            letterAsciiBinary = "0" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 6) {
            letterAsciiBinary = "00" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 5) {
            letterAsciiBinary = "000" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 4) {
            letterAsciiBinary = "0000" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 3) {
            letterAsciiBinary = "00000" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 2) {
            letterAsciiBinary = "000000" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else if (letterAsciiBinary.length() == 1) {
            letterAsciiBinary = "0000000" + letterAsciiBinary.substring(0, letterAsciiBinary.length());
        } else {
            System.out.println("Error: Something is incredibly wrong");
        }
        return letterAsciiBinary;
    }

    /**
     * HEHE This is a method that I used for debugging. It prints out the RGB
     * values in binary. I used this to check the last bit of every RGB value in
     * every pixel. The order RGB matters.
     *
     * @param newPixels
     */
    private void printBins(int[] newPixels) {
        System.out.println("Printing Bins");
        for (int i = 0; i < newPixels.length; i++) {
            Color c = new Color(newPixels[i]);
            System.out.println("r: " + Integer.toBinaryString(c.getRed()));
            System.out.println("g: " + Integer.toBinaryString(c.getGreen()));
            System.out.println("b: " + Integer.toBinaryString(c.getBlue()));
        }
    }

    /**
     * This method ensures that the flags entered were valid, and then assigns a
     * flagCode to the flag codes that were passed in.
     *
     * @param flags - see the documentation for selectableSteganographi()
     * @return
     */
    private int verifyFlags(String flags) {
        if (flags.equals("-rgb") || flags.equals("-rbg") || flags.equals("-grb")
                || flags.equals("-gbr") || flags.equals("-brg")
                || flags.equals("-bgr") || flags.equals("-RGB")
                || flags.equals("-RBG") || flags.equals("-GRB")
                || flags.equals("-GBR") || flags.equals("-BRG")
                || flags.equals("-BGR")) {
            return 0;
        } else if (flags.equals("-rg") || flags.equals("-RG")
                || flags.equals("-gr") || flags.equals("-GR")) {
            return 1;
        } else if (flags.equals("-rb") || flags.equals("-RB")
                || flags.equals("-br") || flags.equals("-BR")) {
            return 2;
        } else if (flags.equals("-gb") || flags.equals("-GB")
                || flags.equals("-bg") || flags.equals("-BG")) {
            return 3;
        } else if (flags.equals("-r") || flags.equals("-R")) {
            return 4;
        } else if (flags.equals("-g") || flags.equals("-G")) {
            return 5;
        } else if (flags.equals("-b") || flags.equals("-B")) {
            return 6;
        } else {
            System.out.println("Error: invalid set of flags - " + flags);
            System.exit(0);
            return -1;
        }
    }

    /**
     * This method adds a 1 to the value of the color pixel that was passed in
     * color. For our purpose, it replaces the 0 with a 1 in the lowest order
     * bit of the right colored byte of its pixel.
     *
     * @param r
     * @param g
     * @param b
     * @param color
     * @return
     */
    private int addABit(int r, int g, int b, String color) {
        if (color.equals("r")) {
            r += 1;
        } else if (color.equals("g")) {
            g += 1;
        } else if (color.equals("b")) {
            b += 1;
        }
        return getRGB(r, g, b);
    }

}
