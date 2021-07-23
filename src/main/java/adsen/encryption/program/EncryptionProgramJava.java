package adsen.encryption.program;

import adsen.encryption.program.encrypters.*;
import adsen.encryption.program.utils.Utils;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class EncryptionProgramJava {
    public static void main(String[] args) {
        System.out.println("Encryption Program Running");

        System.out.println((char)97);
        System.out.println((int)'z');

        Scanner scanner = new Scanner(System.in);

        String input = "";

        while (!input.equalsIgnoreCase("end")) {
            System.out.println("What do you want to do?");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("encrypt")) {

                System.out.println("Please input what encryption method you want to use");
                String encoding = scanner.nextLine();

                switch (encoding.toLowerCase(Locale.ROOT)) {
                    case "expanded_vignere" -> {
                        System.out.println("Please input the keys for the encoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encrypter vignere = new ExpandedVignere(keys);
                        String message = Utils.getMessageInput(scanner, true);
                        String encryptedMessage = vignere.encrypt(message);
                        System.out.println("Encrypted message: " + encryptedMessage);
                    }
                    case "classic_vignere" -> {
                        System.out.println("Please input the key for the encoder");
                        String key = scanner.nextLine();
                        Encrypter vignere = new ClassicVignere(key);
                        String message = Utils.getMessageInput(scanner, true);
                        String encryptedMessage = vignere.encrypt(message);
                        System.out.println("Encrypted message: " + encryptedMessage);
                    }
                    case "huffman" -> {
                        Encrypter huffman = new Huffman();
                        String message = Utils.getMessageInput(scanner, true);
                        String encryptedMessage = huffman.encrypt(message);
                        System.out.println("Encrypted message: " + encryptedMessage);
                    }
                    case "regular_ol_scrambling" ->{
                        Encrypter scrambler = new RegularOlScrambling(0);//using seed 0, may change to something else later
                        String message = Utils.getMessageInput(scanner, true);
                        System.out.println("Encrypted message: " + scrambler.decrypt(message));
                    }
                    case "master" -> {//best encryption I can provide, essentially combining all the ones I have
                        String keys = "";

                        System.out.println("Please input your email address:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);
                        System.out.println("Please input the email address of the recipient:");
                        keys += " " + scanner.nextLine().replaceAll(" ", "").toLowerCase(Locale.ROOT);

                        Encrypter huffman = new Huffman();

                        String message = Utils.getMessageInput(scanner, true);

                        String[] huffmanEncodedMessageAndTree = huffman.encrypt(message).split("\n");
                        String huffmanTree = huffmanEncodedMessageAndTree[0];
                        String huffmanEncodedMessage = huffmanEncodedMessageAndTree[2];//todo maybe use this to seed RegularOlScrambling's RNG, as it's a number

                        keys += " " + huffmanTree;

                        Encrypter vignere = new ExpandedVignere(keys.split(" "));

                        String fullyEncryptedMessage = vignere.encrypt(huffmanEncodedMessage);

                        System.out.println("Encrypted message:\n" + huffmanTree + "\n\n" + fullyEncryptedMessage);
                    }
                    default -> System.out.printf("Unknown encryption method '%s'\n", encoding);
                }
            } else if (input.equalsIgnoreCase("decrypt")) {

                System.out.println("Please input what encryption method you want to use");
                String decoding = scanner.nextLine();

                switch (decoding.toLowerCase(Locale.ROOT)) {
                    case "expanded_vignere" -> {
                        System.out.println("Please input the keys for the decoder, separated by a space bar");
                        String[] keys = scanner.nextLine().split(" ");
                        Encrypter vignere = new ExpandedVignere(keys);
                        String encryptedMessage = Utils.getMessageInput(scanner, false);
                        String decryptedMessage = vignere.decrypt(encryptedMessage);

                        System.out.println("Decrypted message: " + decryptedMessage);
                    }
                    case "classic_vignere" -> {
                        System.out.println("Please input the key for the decoder");
                        String key = scanner.nextLine();
                        Encrypter vignere = new ClassicVignere(key);
                        String encryptedMessage = Utils.getMessageInput(scanner, false);
                        String decryptedMessage = vignere.decrypt(encryptedMessage);

                        System.out.println("Decrypted message: " + decryptedMessage);
                    }
                    case "huffman" -> {
                        System.out.println("Please input the message's decoding tree");
                        String tree = scanner.nextLine();
                        Encrypter huffman = new Huffman();
                        String message = Utils.getMessageInput(scanner, false);
                        String decryptedMessage = huffman.decrypt(tree + "\n\n" + message);
                        System.out.println("Decrypted message: " + decryptedMessage);
                    }
                    case "regular_ol_scrambling" ->{
                        Encrypter scrambler = new RegularOlScrambling(0);//using seed 0, may change to something else later
                        String message = Utils.getMessageInput(scanner, false);
                        System.out.println("Decrypted message: " + scrambler.decrypt(message));
                    }
                    case "master" -> {

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

                        Encrypter vignere = new ExpandedVignere(keys.split(" "));
                        String huffmanEncodedMessage = vignere.decrypt(encryptedMessage);

                        Encrypter huffman = new Huffman();
                        String fullyDecodedMessage = huffman.decrypt(huffmanTree + "\n\n" + huffmanEncodedMessage);

                        System.out.println("Decrypted message: " + fullyDecodedMessage);
                    }
                    default -> System.out.printf("Unknown encryption method '%s'\n", decoding);
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
