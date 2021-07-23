package adsen.encryption.program.encrypters;


import static adsen.encryption.program.utils.Utils.CharScrambleUtils.CHARACTER_MAP;
import static adsen.encryption.program.utils.Utils.CharScrambleUtils.CHARACTERS;

import java.util.Scanner;

import static java.lang.Math.floorMod;


/**
 * Regular vignere encoding, with no frills added.
 */
public class ClassicVignere implements Encrypter {
    private final String key;

    public ClassicVignere(Scanner scanner, String inputKey) {
        while (!isValidKey(inputKey)) {//Makes sure user has inputted valid key
            System.out.println("Invalid key '" + inputKey + "', please input a valid one (containing only upper and " +
                    "lowercase Latin letters, as well as ! and ?");
            inputKey = scanner.nextLine();
        }

        this.key = inputKey;
    }


    @Override
    public String encrypt(String input) {
        char[] encodedString = new char[input.length()];

        for (int i = 0; i < input.toCharArray().length; i++) {
            char c = input.charAt(i);
            if (CHARACTER_MAP.containsKey(c)) {//means we can encode this
                int charIndex = CHARACTER_MAP.get(c);
                int keyCharIndex = CHARACTER_MAP.get(key.charAt(i % key.length()));
                encodedString[i] = CHARACTERS[floorMod(charIndex + keyCharIndex, 64)];
            } else {
                encodedString[i] = c;
            }
        }

        return new String(encodedString);
    }

    @Override
    public String decrypt(String encodedInput) {
        char[] decodedString = new char[encodedInput.length()];

        for (int i = 0; i < encodedInput.toCharArray().length; i++) {
            char c = encodedInput.charAt(i);
            if (CHARACTER_MAP.containsKey(c)) {//means we can encode this
                int charIndex = CHARACTER_MAP.get(c);
                int keyCharIndex = CHARACTER_MAP.get(key.charAt(i % key.length()));
                decodedString[i] = CHARACTERS[floorMod(charIndex - keyCharIndex, 64)];
            } else {
                decodedString[i] = c;
            }
        }

        return new String(decodedString);
    }

    boolean isValidKey(String key) {
        for (char c : key.toCharArray()) {
            if (!CHARACTER_MAP.containsKey(c)) return false;
        }
        return true;
    }
}
