package adsen.encryption.program.encoders;

/**
 * The inheritor class from which all encoders must derive
 */
public interface Encoder {
    /**
     * Allows to scramble a given string however you want. Should be decode-able using {@link Encoder#decode}
     *
     * @param input The string we want to scramble
     * @return the scrambled key
     */
    public String encode(String input);

    /**
     * Allows to de-scramble a string scrambled with {@link Encoder#encode} method. Ideally, calling {@code Encoder.decode(Encoder.encode(inputString))}
     * should return the inputted string
     *
     * @param encodedInput The scrambled message we want to unscramble
     * @return The inputted string
     */
    public String decode(String encodedInput);
}
