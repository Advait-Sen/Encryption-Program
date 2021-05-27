package adsen.encryption.program.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The utils class used to scramble chars and store them for vignere encoding
 */
public class CharScrambleUtils {//todo think of a better name for this

    /**
     * A simple array of 64 chars which can be displayed and therefore easily copy-pasted or typed onto any keyboard. This
     * is useful because the characters themselves are arbitrary and useless, all that matters is the bit representation
     * of their index within the array.
     */
    public static char[] CHARACTERS = new char[]{
            '?', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!',
    };

    /**
     * A map of the chars in {@link CharScrambleUtils#CHARACTERS} to their indexes, as it was the simplest way to provide
     * a 2-way mapping.
     */
    public static Map<Character, Integer> CHARACTER_MAP = new HashMap<>();

    static {
        for (int i = 0; i < CHARACTERS.length; i++) {
            char c = CHARACTERS[i];
            CHARACTER_MAP.put(c, i);
        }
    }

    /**
     * Turns an array of chars into a string, which is easy to display to the user. To undo it, use
     * {@link CharScrambleUtils#simpleStringToCharArray}.It grabs the bits of the char into 3 numbers: bits 0-5, 6-11 and
     * 12-15, and then stores them in 3 elements of achar array, where each element is at most 63. Then it replaces each
     * with the corresponding char in {@link CharScrambleUtils#CHARACTERS} so it can be stored and retrieved with ease.
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
}
