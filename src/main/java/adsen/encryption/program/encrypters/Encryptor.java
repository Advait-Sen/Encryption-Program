package adsen.encryption.program.encrypters;

import java.util.Random;

/**
 * The inheritor class from which all encoders must derive
 */
public interface Encryptor {

    /**
     * A random which the encoders can use to generate random numbers however they wish. It's just declared here to
     * reduce the number of useless variable declarations.
     */
    Random random = new Random();

    /**
     * Allows to scramble a given string however you want. Should be decrypt-able using {@link Encryptor#decrypt}
     *
     * @param input The string we want to scramble
     * @return the scrambled message
     */
    String encrypt(String input);

    /**
     * Allows to de-scramble a string scrambled with {@link Encryptor#encrypt} method. Ideally, calling {@code Encryptor.decrypt(Encryptor.encrypt(inputString))}
     * should return the inputted string
     *
     * @param encodedInput The scrambled message we want to unscramble
     * @return The inputted string
     */
    String decrypt(String encodedInput);
}
