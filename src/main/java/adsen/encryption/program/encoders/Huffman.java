package adsen.encryption.program.encoders;

import adsen.encryption.program.utils.CharTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class Huffman implements Encoder {

    public Huffman() {

    }

    @Override
    public String encode(String input) {
        char[] charList = input.toCharArray();

        Map<Character, Integer> charFrequency = new HashMap<>();

        for (char c : charList) {
            charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }

        List<CharTree> sortedCharacters = charFrequency.keySet().stream().
                sorted((c1, c2) -> Integer.compare(charFrequency.get(c2), charFrequency.get(c1))).
                map(CharTree::new).collect(Collectors.toList());
        List<CharTree> childBuffer = new ArrayList<>();

        while (sortedCharacters.size() > 9) {
            for (int i = 0; i < 10; i++) {
                childBuffer.add(sortedCharacters.remove(sortedCharacters.size() - 1));
            }

            sortedCharacters.add(new CharTree(childBuffer));
            childBuffer.clear();
            sortedCharacters.sort((ct1, ct2) -> Integer.compare(ct2.getFrequency(), ct1.getFrequency()));
        }

        CharTree huffmanTree = new CharTree(sortedCharacters);

        StringBuilder encodedString = new StringBuilder(huffmanTree.toString().replaceAll("\n","\\\\n") + "\n\n");

        for (char c : charList) {
            encodedString.append(huffmanTree.charPath(c));
        }

        return encodedString.toString();
    }

    @Override
    public String decode(String encodedInput) {

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
