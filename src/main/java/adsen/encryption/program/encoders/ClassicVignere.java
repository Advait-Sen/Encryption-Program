package adsen.encryption.program.encoders;

import adsen.encryption.program.utils.Utils.CharScrambleUtils;

import java.util.Locale;

/**
 * Regular vignere encoding, but expanded to use any character, using less lookup tables and more modular arithmetic.
 * todo test that this works
 */
public class ClassicVignere implements Encoder {
    private final String key;

    public ClassicVignere(String key) {
        this.key = key.toLowerCase(Locale.ROOT);//lowercase to help simplify the maths
    }


    @Override
    public String encode(String input) {
        char[] encodedString = new char[input.length()];
        //putting to lower case to simplify the maths
        char[] inputCharsLower = input.toLowerCase(Locale.ROOT).toCharArray();

        for (int i = 0; i < inputCharsLower.length; i++) {
            char c = inputCharsLower[i];
            boolean isCharUpper = Character.isUpperCase(input.charAt(i));//used to keep track of upper and lower case
            //adding the two characters and subtracting 96, cos lowercase 'a' is 97 in chars, so now we can just check if the number has gone above 122, subtract 26
            int charSum = (c + key.charAt(i % key.length())) - 96;
            char encodedChar = (char) (charSum > 122 ? charSum - 26 : charSum);
            encodedString[i] = !Character.isAlphabetic(c) ? c : isCharUpper ? Character.toUpperCase(encodedChar) : encodedChar;
        }

        return new String(encodedString);
    }

    @Override
    public String decode(String encodedInput) {
        char[] encodedString = new char[encodedInput.length()];
        //putting to lower case to simplify the maths
        char[] inputCharsLower = encodedInput.toLowerCase(Locale.ROOT).toCharArray();

        for (int i = 0; i < inputCharsLower.length; i++) {
            char c = inputCharsLower[i];
            boolean isCharUpper = Character.isUpperCase(encodedInput.charAt(i));//used to keep track of upper and lower case
            //adding the two characters and subtracting 96, cos lowercase 'a' is 97 in chars, so now we can just check if the number has gone above 122, subtract 26
            int charSum = (c - key.charAt(i % key.length())) - 96;
            char encodedChar = (char) (charSum > 122 ? charSum - 26 : charSum);
            encodedString[i] = !Character.isAlphabetic(c) ? c : isCharUpper ? Character.toUpperCase(encodedChar) : encodedChar;
        }

        return new String(encodedString);
    }
}
