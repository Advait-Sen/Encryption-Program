package adsen.encryption.program;

import adsen.encryption.program.encoders.Encoder;
import adsen.encryption.program.encoders.Huffman;
import adsen.encryption.program.encoders.Vignere;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class EncryptionProgramJava {
    public static void main(String[] args) {
        System.out.println("Encryption Program Running");

        Scanner scanner = new Scanner(System.in);

        String input = "";

        while (!input.equalsIgnoreCase("end")) {
            System.out.println("What do you want to do?");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("encode")) {

                System.out.println("Please input what encryption method you want to use");
                String encoding = scanner.nextLine();

                switch (encoding.toLowerCase(Locale.ROOT)) {

                    case "vignere": {
                        System.out.println("Please input the keys for the encoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encoder vignere = new Vignere(keys);
                        System.out.println("Please input the message you want to encrypt:");
                        String message = scanner.nextLine();
                        String encryptedMessage = vignere.encode(message);
                        System.out.println("Encrypted message: " + encryptedMessage);

                        break;
                    }

                    case "huffman": {
                        System.out.println("Please input the message you want to encode");
                        Encoder huffman = new Huffman();
                        String message = scanner.nextLine();
                        String encryptedMessage = huffman.encode(message);
                        System.out.println("Encrypted message: " + encryptedMessage);

                        break;
                    }

                    default:
                        System.out.printf("Unknown encryption method '%s'\n", encoding);
                }
            } else if (input.equalsIgnoreCase("decode")) {

                System.out.println("Please input what encryption method you want to use");
                String decoding = scanner.nextLine();

                switch (decoding.toLowerCase(Locale.ROOT)) {

                    case "vignere": {
                        System.out.println("Please input the keys for the decoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encoder vignere = new Vignere(keys);
                        System.out.println("Please input the message you want to decrypt:");
                        String encryptedMessage = scanner.nextLine();
                        String decryptedMessage = vignere.decode(encryptedMessage);

                        System.out.println("Decrypted message: " + decryptedMessage);

                        break;
                    }

                    case "huffman": {
                        System.out.println("Please input the message's decoding tree");
                        String tree = scanner.nextLine();
                        System.out.println("Please input the message you want to decode");
                        Encoder huffman = new Huffman();
                        String message = scanner.nextLine();
                        String decryptedMessage = huffman.decode(tree + "\n\n" + message);
                        System.out.println("Decrypted message: " + decryptedMessage);

                        break;
                    }


                    default:
                        System.out.printf("Unknown encryption method '%s'\n", decoding);
                }
            } else if (!input.equals("end"))
                System.out.println("Unknown action '" + input + "'");
        }

        scanner.close();
    }

    /**
     * Generates a completely random string each time to be used as deterministic salt for anything, really
     *
     * @param length The length of the random string
     * @return A random string
     */
    public static String randomSalt(int length) {
        Random random = new Random(length);
        char[] stringChars = new char[length];
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ((random.nextLong() >> 48 ^ random.nextLong() >> 32 ^ random.nextLong() >> 16 ^ random.nextLong()) & 0xFFFF);
            stringChars[i] = randomChar;
        }

        return new String(stringChars);
    }
}
