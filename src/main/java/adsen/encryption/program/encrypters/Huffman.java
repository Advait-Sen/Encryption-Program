package adsen.encryption.program.encrypters;

import adsen.encryption.program.utils.CharTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class Huffman implements Encryptor {

    public Huffman() {

    }

    @Override
    public String encrypt(String input) {
        char[] charList = input.toCharArray();

        Map<Character, Integer> charFrequency = new HashMap<>();

        for (char c : charList) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }
        System.out.println("charFrequency = " + charFrequency);
        List<CharTree> sortedCharacters = charFrequency.keySet().stream().
                sorted((c1, c2) -> Integer.compare(charFrequency.get(c2), charFrequency.get(c1))).
                map(c -> new CharTree(c, charFrequency.get(c))).collect(Collectors.toList());
        System.out.println("sortedCharacters = " + sortedCharacters);

        List<CharTree> childBuffer = new ArrayList<>();

        while (sortedCharacters.size() > 16) {
            for (int i = 0; i < Math.min(16, sortedCharacters.size() - 15); i++) {
                childBuffer.add(sortedCharacters.remove(sortedCharacters.size() - 1));
            }

            sortedCharacters.add(new CharTree(childBuffer));
            childBuffer.clear();
            sortedCharacters.sort((ct1, ct2) -> Integer.compare(ct2.getFrequency(), ct1.getFrequency()));
        }

        CharTree huffmanTree = new CharTree(sortedCharacters);

        StringBuilder encodedString = new StringBuilder(huffmanTree.toString().replaceAll("\n", "\\\\n") + "\n\n");

        for (char c : charList) {
            encodedString.append(huffmanTree.charPath(c));
        }

        return encodedString.toString();
    }

    @Override
    public String decrypt(String encodedInput) {

        String[] treeAndMessage = encodedInput.split("\n");
        CharTree huffmanTree;
        try {
            huffmanTree = CharTree.parseString(treeAndMessage[0]);
        } catch (Exception exc) {
            System.out.println("Invalid tree: " + treeAndMessage[0]);
            return "";
        }

        String encodedMessage = treeAndMessage[2].replaceAll(" ", "");
        if (!encodedMessage.matches("^\\d+")) {
            System.out.println("Message must be a number: " + encodedMessage);
            return "";
        }
        StringBuilder decodedMessage = new StringBuilder();
        Stack<CharTree> branches = new Stack<>();
        branches.push(huffmanTree);
        CharTree currentBranch;

        for (char c : encodedMessage.toCharArray()) {
            currentBranch = branches.peek();

            int index = Integer.decode(String.valueOf(c));
            if (currentBranch.hasChild(index)) {
                if (currentBranch.getChar(String.valueOf(c)) == 0)
                    branches.push(currentBranch.getChild(index));
                else {
                    decodedMessage.append(currentBranch.getChar(String.valueOf(c)));
                    while (branches.size() > 1)//going back down the tree
                        branches.pop();
                }
            } else {
                System.out.println("Invalid message: " + encodedMessage);
                return "";
            }
        }

        return decodedMessage.toString();
    }
}
