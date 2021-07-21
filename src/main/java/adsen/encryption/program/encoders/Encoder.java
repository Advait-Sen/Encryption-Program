package adsen.encryption.program.encoders;

import java.util.Random;

/**
 * The inheritor class from which all encoders must derive
 */
public interface Encoder {

    /**
     * A random which the encoders can use to generate random numbers however they wish. It's just declared here to
     * reduce the number of useless variable declarations.
     */
    Random random = new Random();

    /**
     * Allows to scramble a given string however you want. Should be decode-able using {@link Encoder#decode}
     *
     * @param input The string we want to scramble
     * @return the scrambled key
     */
    String encode(String input);

    /**
     * Allows to de-scramble a string scrambled with {@link Encoder#encode} method. Ideally, calling {@code Encoder.decode(Encoder.encode(inputString))}
     * should return the inputted string
     *
     * @param encodedInput The scrambled message we want to unscramble
     * @return The inputted string
     */
    String decode(String encodedInput);
}
