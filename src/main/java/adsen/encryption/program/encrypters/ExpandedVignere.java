package adsen.encryption.program.encrypters;

import adsen.encryption.program.utils.Utils.CharScrambleUtils;

/**
 * An encryption system inspired by regular vignere encoding, but expanded to use any characters so it works more effectively,
 * using less lookup tables and more modular arithmetic.
 */
public class ExpandedVignere implements Encryptor {
    private final String[] keys;
    private final long hashCodeLong;

    public ExpandedVignere(String... keys) {
        this.keys = keys;
        this.hashCodeLong = this.hashCodeLong();
    }


    @Override
    public String encrypt(String input) {
        random.setSeed(this.hashCodeLong);

        char[] encodedString = new char[input.length()];
        char[] inputChars = input.toCharArray();

        for (int i = 0; i < inputChars.length; i++) {
            char c = inputChars[i];
            //Using mod operator cos 0xFFFF (or 2^16-1 in decimal) is the maximum value a char can be, so capping it at
            // 0x10000 (2^16) means we won't overflow
            char encodedChar = (char) Math.floorMod(c + random.nextInt(), 0x10000);
            encodedString[i] = encodedChar;
        }

        return CharScrambleUtils.charArrayToSimpleString(encodedString);
    }

    @Override
    public String decrypt(String encodedInput) {
        random.setSeed(this.hashCodeLong);

        StringBuilder decodedString = new StringBuilder();

        for (char c : CharScrambleUtils.simpleStringToCharArray(encodedInput)) {
            //To encrypt we added the random.nextInt(), so here we subtract. In essence, it's the same exact number
            char decodedChar = (char) Math.floorMod(c - random.nextInt(), 0x10000);
            decodedString.append(decodedChar);
        }

        return decodedString.toString();
    }

    @Override
    public int hashCode() {
        int code = 0;

        for (String key : keys) {
            int keyHash = key.hashCode();
            code ^= keyHash << 24 ^ keyHash << 16 ^ keyHash << 8 ^ keyHash ^ keyHash >> 8 ^ keyHash >> 16 ^ keyHash >> 24;
        }

        return code;
    }

    /**
     * The {@code long} version of the hashcode, used to set the random's seed.
     */
    public long hashCodeLong() {
        int hashCode = this.hashCode();
        return (long) hashCode << 52 ^ (long) hashCode << 48 ^ (long) hashCode << 40 ^ (long) hashCode << 32 ^
                (long) hashCode << 24 ^ (long) hashCode << 16 ^ (long) hashCode << 8 ^ hashCode ^ hashCode >> 8 ^
                hashCode >> 16 ^ hashCode >> 24;
    }
}
