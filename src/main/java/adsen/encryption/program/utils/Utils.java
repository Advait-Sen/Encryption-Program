package adsen.encryption.program.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Utils {
    /**
     * Read's a user input from a {@link Scanner} object, which is useful as it means that it needn't come from the command
     * line and can be shifted elsewhere if need be.
     *
     * @param encrypt To improve the message (i.e tell whether the user wants to encrypt or decrypt something)
     * @return The user's message
     */
    public static String getMessageInput(Scanner scanner, boolean encrypt) {
        StringBuilder message = new StringBuilder();
        String input;
        System.out.println(
                "Please input the message you want to " + (encrypt ? "encrypt" : "decrypt") +
                        " line by line, and when you're done enter 'END' (case-sensitive)"
        );

        while (true) {
            input = scanner.nextLine();
            if (input.equals("END"))
                break;
            message.append(input);
            message.append('\n');
        }

        message = new StringBuilder(message.toString().strip());//removing initial and final whitespaces

        return message.toString();
    }

    /**
     * Handy function to resize 2d array, found here: <a href="https://stackoverflow.com/a/27728645">Stackoverflow</a>
     * I repurposed it to handle any 2d object array, as that happened to be what I needed.
     *
     * @param matrix The input 2d array
     * @param w      The new width of the array
     * @param h      The new height of the array
     * @return The new 2d array, resized with nulls in the new positions (if it expanded)
     */
    public static Object[][] resize(Object[][] matrix, int w, int h) {
        Object[][] temp = new Object[h][w];
        h = Math.min(h, matrix.length);
        w = Math.min(w, matrix[0].length);
        for (int i = 0; i < h; i++)
            System.arraycopy(matrix[i], 0, temp[i], 0, w);
        return temp;
    }

    /**
     * A class of utility functions to help scramble characters and store characters which the computer may not be able
     * to display into a neater format
     */
    public static class CharScrambleUtils {

        /**
         * A simple array of 64 chars which can be displayed and therefore easily copy-pasted or typed onto any keyboard. This
         * is useful because the characters themselves are arbitrary and useless, all that matters is the bit representation
         * of their index within the array.
         * These are also the characters that the {@link adsen.encryption.program.encrypters.ClassicVignere#encrypt} can encrypt.
         */
        public static char[] CHARACTERS = new char[]{
                '?', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
                'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', '!',
        };

        /**
         * A map of the chars in {@link CharScrambleUtils#CHARACTERS} to their indexes, as it was the simplest way to provide
         * a 2-way mapping.
         */
        public static Map<Character, Integer> CHARACTER_MAP = new HashMap<>();

        static {//todo replace with DoubleWayMapping so I don't have to do this.
            for (int i = 0; i < CHARACTERS.length; i++) {
                char c = CHARACTERS[i];
                CHARACTER_MAP.put(c, i);
            }
        }

        /**
         * Turns an array of chars into a string, which is easy to display to the user. To undo it, use
         * {@link CharScrambleUtils#simpleStringToCharArray}.It grabs the bits of the char into 3 numbers: bits 0-5, 6-11
         * and 12-15, and then stores them in 3 elements of a char array, where each element is at most 63. Then it replaces
         * each with the corresponding char in {@link CharScrambleUtils#CHARACTERS} so it can be stored and retrieved
         * with ease.
         *
         * @param charArray The array of chars we want to turn into a string
         * @return The numeric string
         */
        public static String charArrayToSimpleString(char[] charArray) {

            char[] ret = new char[charArray.length * 3];

            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                //storing bits 0-5, 6-11 and 12-15 of the char respectively into the three positions in the char array
                ret[i * 3] = CHARACTERS[(char) (c & 0x3F)];
                ret[i * 3 + 1] = CHARACTERS[(char) ((c & 0xFC0) >> 6)];
                ret[i * 3 + 2] = CHARACTERS[(char) ((c & 0xF000) >> 12)];
            }

            return new String(ret);
        }

        /**
         * Takes each triplet of chars in the simple string's char array, and then takes the corresponding index of the char
         * in {@link CharScrambleUtils#CHARACTERS} (using {@link CharScrambleUtils#CHARACTER_MAP} for faster lookup) to build
         * the correct decoded char using the 1st element in each triplet as bits 0-5, the second as bits 6-11 and the third
         * as bits 12-15 to fully reconstruct the original string. In case this explanation is too complex (which, let's face
         * it, it probably is), read the code to understand better.
         *
         * @param string The encoded string
         * @return The array of characters encoded with the {@link CharScrambleUtils#charArrayToSimpleString} functions
         */
        public static char[] simpleStringToCharArray(String string) {
            //cos it may have shortened the string, taking out excess chars with a value of 0, which are important here
            int charLength = (int) Math.ceil(((double) string.length()) / 3);
            char[] inputChars = Arrays.copyOf(string.toCharArray(), charLength * 3);

            char[] ret = new char[charLength];

            for (int i = 0; i < charLength; i++) {
                ret[i] = (char) (CHARACTER_MAP.getOrDefault(inputChars[i * 3], 0) | (CHARACTER_MAP.getOrDefault(inputChars[i * 3 + 1], 0) << 6) | (CHARACTER_MAP.getOrDefault(inputChars[i * 3 + 2], 0) << 12));
            }

            return ret;
        }

        /**
         * Simply swaps 2 characters in a string
         *
         * @param inputString Original string
         * @return Original string, but with 2 characters swapped
         */
        public static String swapCharacter(String inputString, int indexOne, int indexTwo) {
            char[] newString = inputString.toCharArray();
            newString[indexOne] = inputString.charAt(indexTwo);
            newString[indexTwo] = inputString.charAt(indexOne);
            return new String(newString);
        }
    }
}

