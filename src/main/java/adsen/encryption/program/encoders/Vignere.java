package adsen.encryption.program.encoders;

import adsen.encryption.program.utils.Utils;

import java.util.Random;

/**
 * An encryption system inspired by regular vignere encoding, but expanded to use any characters so it works more effectively,
 * using less lookup tables and more modular arithmetic.
 */
public class Vignere implements Encoder {
    private final String[] keys;
    private final Random random;
    private final long hashCodeLong;

    public Vignere(String... keys) {
        this.keys = keys;
        this.random = new Random();
        this.hashCodeLong = this.hashCodeLong();
    }


    @Override
    public String encode(String input) {
        random.setSeed(this.hashCodeLong);

        char[] encodedString = new char[input.length()];
        char[] inputChars = input.toCharArray();

        for (int i = 0; i < inputChars.length; i++) {
            char c = inputChars[i];
            char encodedChar = (char) Math.floorMod(c + random.nextInt(), 0x10000);
            encodedString[i] = encodedChar;
        }

        return Utils.charArrayToSimpleString(encodedString);
    }

    @Override
    public String decode(String encodedInput) {
        random.setSeed(this.hashCodeLong);

        StringBuilder decodedString = new StringBuilder();

        for (char c : Utils.simpleStringToCharArray(encodedInput)) {
            char decodedChar = (char) Math.floorMod(c - random.nextInt(), 0x10000);
            decodedString.append(decodedChar);
        }

        return decodedString.toString();
    }

    @Override
    public int hashCode() {
        int code = 0;

        for (String key : keys)
            code ^= key.hashCode();

        return code;
    }

    /**
     * The {@code long} version of the hashcode, used to set the random's seed.
     */
    public long hashCodeLong() {
        int hashCode = this.hashCode();
        return (long) hashCode << 32 ^ (long) hashCode << 16 ^ hashCode;
    }
}
