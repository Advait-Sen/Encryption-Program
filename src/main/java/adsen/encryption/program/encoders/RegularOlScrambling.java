package adsen.encryption.program.encoders;

import adsen.encryption.program.utils.Utils.CharScrambleUtils;

/**
 * Just scrambles the characters of a message in a reversible manner
 * todo test
 */
public class RegularOlScrambling implements Encoder {

    long randomSeed;

    public RegularOlScrambling(long seed) {
        randomSeed = seed;
    }

    /**
     * Randomly rearranges the letters by generating a series of numbers equal to the number of characters in the number.
     * The maximum size of these numbers decreases by 1 each time, starting with the length of the string and going down
     * to two. The characters are then swapped with the ones in the random positions as we go along, using
     * {@link CharScrambleUtils#swapCharacter}
     *
     * @param input The string we want to scramble
     * @return the scrambled message
     */
    @Override
    public String encode(String input) {
        random.setSeed(randomSeed);
        String encodedString = input;
        int inputLength = input.length();
        for (int length = inputLength; length > 0; length--) {
            int i = inputLength - length;
            encodedString = CharScrambleUtils.swapCharacter(encodedString, i, random.nextInt(length));
        }
        return encodedString;
    }

    /**
     * This generates the same numbers as the {@link RegularOlScrambling#encode} function does to scramble the message's
     * characters (provided the same seed and correct encrypted message obviously), and then applies the same swaps in reverse
     * order to restore the message back to its original state.
     * @param encodedInput The scrambled message we want to unscramble
     */
    @Override
    public String decode(String encodedInput) {
        random.setSeed(randomSeed);
        String decodedString = encodedInput;
        int inputLength = encodedInput.length();
        int[] randomSwaps = new int[inputLength];
        for (int length = inputLength - 1; length >= 0; length--) {
            randomSwaps[length] = random.nextInt(length + 1);
        }
        for (int i = 0; i < randomSwaps.length; i++) {
            int randomSwapIndex = randomSwaps[i];
            decodedString = CharScrambleUtils.swapCharacter(decodedString, randomSwapIndex, i);
        }
        return decodedString;
    }
}
