package adsen.encryption.program.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CharTree {
    private final CharTree[] children;
    private final char charValue;
    /**
     * A cache of the paths to the characters, to make lookups faster and ore efficient, and to avoid repeating the same
     * search over and over.
     */
    private Map<Character, String> charCache = new HashMap<>();
    private int frequency;

    public CharTree(char charValue) {
        this.charValue = charValue;

        children = new CharTree[0];

        this.frequency = 1;
    }

    public CharTree(CharTree child) {
        this.charValue = child.charValue;
        this.children = child.children;
        this.frequency = child.frequency;
        this.charCache = child.charCache;
    }

    public CharTree(List<CharTree> children) {
        if (children.size() > 1) {
            this.charValue = 0;
            this.children = children.toArray(new CharTree[0]);
            this.frequency = 0;
            for (int i = 0; i < children.size(); i++) {
                CharTree child = children.get(i);
                this.frequency += child.frequency;
                int index = i;
                child.charCache.forEach((c, s) -> this.charCache.put(c, index + s));
            }
        } else if (children.size() == 1) {
            CharTree child = children.get(0);
            this.charValue = child.charValue;
            this.children = child.children;
            this.frequency = child.frequency;
            this.charCache = child.charCache;
        } else
            throw new IllegalArgumentException("Cannot input empty list as a CharTree!");
    }

    public boolean hasChildren() {
        return hasChild(0);
    }

    public boolean hasChild(int index) {
        return index < children.length;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public CharTree getChild(int index) {
        return children[index];
    }

    public char getChar(String path) {
        if (path.equals(""))
            return this.charValue;

        for (Map.Entry<Character, String> entry : charCache.entrySet()) {
            if (entry.getValue().equals(path))
                return entry.getKey();
        }

        return children[Integer.decode(String.valueOf(path.charAt(0)))].getChar(path.replaceFirst(String.valueOf(path.charAt(0)),""));
    }

    public boolean hasChar(char c) {
        return this.charValue == c || Arrays.stream(children).anyMatch(child -> child.hasChar(c));
    }

    public String charPath(char c) {
        if (charCache.containsKey(c)) {
            return charCache.get(c);
        }

        for (int i = 0; i < children.length; i++) {
            CharTree child = children[i];
            if (child != null && child.hasChar(c)) {
                charCache.put(c, i + child.charPath(c));
                return charCache.get(c);
            }
        }

        charCache.put(c, "");
        return charCache.get(c);
    }

    public int size() {
        return children.length;
    }

    /**
     * Returns a boolean indicating whether or not this {@link CharTree}
     */
    public boolean isLeaf() {
        return charValue != 0;
    }

    /**
     * A string representation of this {@link CharTree} object, which is entirely re-traceable into a valid {@link CharTree}
     * object via the {@link CharTree#parseString} method.
     */
    @Override
    public String toString() {

        if (charValue != 0)
            return "'" + charValue + "'";

        StringBuilder string = new StringBuilder("[");

        for (int i = 0; i < children.length - 1; i++) {
            string.append(children[i].toString()).append(",");
        }
        string.append(children[children.length - 1].toString()).append("]");

        return string.toString();
    }

    @Override
    public int hashCode() {
        if (charValue != 0)
            return charValue;
        int hash = 0;
        for (CharTree child : children) {
            hash ^= child.hashCode();
        }

        return hash;
    }

    /**
     * Parses a {@link CharTree} from an input string representation
     *
     * @param treeString The string representation of the string generated with the {@link CharTree#toString()} method
     * @return A valid CharTree object, if one was able to be constructed. If not, returns null
     * @throws IllegalStateException If there is an error in the given string
     */

    public static CharTree parseString(String treeString) {
        if(treeString.equals("'\\n'"))//to handle newlines
            return new CharTree('\n');

        if (treeString.matches("'.'"))
            return new CharTree(treeString.charAt(1));

        List<String> childArgs = new ArrayList<>();

        StringBuilder child = new StringBuilder();

        char[] treeChars = treeString.toCharArray();
        int parenCounter = 1;
        boolean isInQuote = false;

        for (int i = 1; i < treeChars.length; i++) {
            char currentChar = treeChars[i];
            if (currentChar == '[')
                parenCounter++;

            if (currentChar == ']')
                parenCounter--;

            if (currentChar == '\'')
                isInQuote = !isInQuote;

            if (parenCounter > 0) {
                child.append(currentChar);
                if (parenCounter == 1 && (currentChar == ']' || currentChar == '\'') && !isInQuote) {
                    childArgs.add(child.toString());
                    child = new StringBuilder();
                    i++;
                }
            }
        }

        return new CharTree(childArgs.stream().map(s -> new CharTree(parseString(s))).collect(Collectors.toList()));
    }
}