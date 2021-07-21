package adsen.encryption.program;

import adsen.encryption.program.encoders.Encoder;
import adsen.encryption.program.encoders.Huffman;
import adsen.encryption.program.encoders.Vignere;
import adsen.encryption.program.utils.Utils;

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
            if (input.equalsIgnoreCase("encrypt")) {

                System.out.println("Please input what encryption method you want to use");
                String encoding = scanner.nextLine();

                switch (encoding.toLowerCase(Locale.ROOT)) {

                    case "vignere": {
                        System.out.println("Please input the keys for the encoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encoder vignere = new Vignere(keys);
                        String message = Utils.getMessageInput(scanner, true);
                        String encryptedMessage = vignere.encode(message);
                        System.out.println("Encrypted message: " + encryptedMessage);

                        break;
                    }

                    case "huffman": {
                        Encoder huffman = new Huffman();
                        String message = Utils.getMessageInput(scanner, true);
                        String encryptedMessage = huffman.encode(message);
                        System.out.println("Encrypted message: " + encryptedMessage);

                        break;
                    }

                    case "master": {//best encryption I can provide, essentially combining all the ones I have
                        String keys = "";

                        System.out.println("Please input your email address:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);
                        System.out.println("Please input the email address of the recipient:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);

                        Encoder huffman = new Huffman();

                        String message = Utils.getMessageInput(scanner, true);

                        String[] huffmanEncodedMessageAndTree = huffman.encode(message).split("\n");
                        String huffmanTree = huffmanEncodedMessageAndTree[0];
                        String huffmanEncodedMessage = huffmanEncodedMessageAndTree[2];//todo add numeric encryption cos this is a number, so it's practically begging for it...

                        keys += " " + huffmanTree;

                        Encoder vignere = new Vignere(keys.split(" "));

                        String fullyEncryptedMessage = vignere.encode(huffmanEncodedMessage);

                        System.out.println("Encrypted message:\n" + huffmanTree + "\n\n" + fullyEncryptedMessage);
                        break;
                    }

                    default:
                        System.out.printf("Unknown encryption method '%s'\n", encoding);
                }
            } else if (input.equalsIgnoreCase("decrypt")) {

                System.out.println("Please input what encryption method you want to use");
                String decoding = scanner.nextLine();

                switch (decoding.toLowerCase(Locale.ROOT)) {

                    case "vignere": {
                        System.out.println("Please input the keys for the decoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encoder vignere = new Vignere(keys);
                        String encryptedMessage = Utils.getMessageInput(scanner, false);
                        String decryptedMessage = vignere.decode(encryptedMessage);

                        System.out.println("Decrypted message: " + decryptedMessage);

                        break;
                    }

                    case "huffman": {
                        System.out.println("Please input the message's decoding tree");
                        String tree = scanner.nextLine();
                        Encoder huffman = new Huffman();
                        String message = Utils.getMessageInput(scanner, false);
                        String decryptedMessage = huffman.decode(tree + "\n\n" + message);
                        System.out.println("Decrypted message: " + decryptedMessage);

                        break;
                    }

                    case "master": {

                        String keys = "";

                        System.out.println("Please input the email address of the sender:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);
                        System.out.println("Please input your email address:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);

                        String fullyEncryptedMessage = Utils.getMessageInput(scanner, false);

                        String[] treeAndEncryptedMessage = fullyEncryptedMessage.split("\n");

                        String huffmanTree = treeAndEncryptedMessage[0];
                        keys += " " + huffmanTree;
                        String encryptedMessage = treeAndEncryptedMessage[2];

                        Encoder vignere = new Vignere(keys.split(" "));
                        String huffmanEncodedMessage = vignere.decode(encryptedMessage);

                        Encoder huffman = new Huffman();
                        String fullyDecodedMessage = huffman.decode(huffmanTree + "\n\n" + huffmanEncodedMessage);

                        System.out.println("Decrypted message: " + fullyDecodedMessage);
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