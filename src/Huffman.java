import java.util.*;

/**
 * Prefix codes and Huffman tree.
 * Tree depends on source data.
 * Sources used: http://enos.itcollege.ee/~ylari/I231/Huffman.java !!!
 * https://www.journaldev.com/23246/huffman-coding-algorithm
 */
public class Huffman {

    private static Map<Character, String> charPrefixHashMap = new HashMap<>();
    private static Map<Byte, Leaf> table;
    private int[] frequencies;
    private int length;

    /** Constructor to build the Huffman code for a given bytearray.
     * @param original source data
     */
    Huffman (byte[] original) {
        frequencies = new int[256];
        table = new HashMap<Byte, Leaf>();
        length = 0;
        init(original);

        Map<Character, Integer> freq = new HashMap<>();
        if (original.length == 1) {
            freq.put((char)original[0], 0);
        }
        for (int i = 0; i < original.length; i++) {
            if (!freq.containsKey((char)original[i])) {
                freq.put((char)original[i], 0);
            }
            freq.put((char)original[i], freq.get((char)original[i]) + 1);
        }

        System.out.println("Frequency map is " + freq.toString());
    }


    private void init(byte[] data) {
        for (byte b : data)
            frequencies[b]++;
        PriorityQueue<Tree> trees = new PriorityQueue<Tree>();
        for (int i = 0; i < frequencies.length; i++)
            if (frequencies[i] > 0)
                trees.offer(new Leaf(frequencies[i], (byte) i));
        assert trees.size() > 0;
        while (trees.size() > 1)
            trees.offer(new Node(trees.poll(), trees.poll()));
        Tree tree = trees.poll();
        setPrefixCodes(tree, new StringBuffer());
    }

    /** Represents Huffman tree. */
    abstract class Tree implements Comparable<Tree> {

        /** The frequency of this tree. */
        public final int frequency;

        public Tree(int frequency) {
            this.frequency = frequency;
        }

        /**
         * This is an overriding method.
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Tree o) {
            return frequency - o.frequency;
        }
    }

    /** Represents Huffman leaf. */
    class Leaf extends Tree {

        /** The byte this leaf represents. */
        final byte value;

        String code;

        Leaf(int frequency, byte value) {
            super(frequency);
            this.value = value;
        }
    }

    /** Represents Huffman node. */
    class Node extends Tree {

        final Tree left, right; // subtrees

        Node(Tree left, Tree right) {
            super(left.frequency + right.frequency);
            this.left = left;
            this.right = right;
        }
    }


    /** Length of encoded data in bits.
     * @return number of bits
     */
    public int bitLength() {
        return length;
    }

    private static void setPrefixCodes(Tree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof Leaf) {
            Leaf leaf = (Leaf) tree;
            leaf.code = (prefix.length() > 0) ? prefix.toString() : "0";
            charPrefixHashMap.put((char)leaf.value, leaf.code);
            table.put(leaf.value, leaf);

        } else if (tree instanceof Node) {
            Node node = (Node) tree;
            prefix.append('0');
            setPrefixCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
            prefix.append('1');
            setPrefixCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /** Encoding the byte array using this prefixcode.
     * @param origData original data
     * @return encoded data
     */
    public byte[] encode (byte [] origData) {
        System.out.println("Character Prefix Map = " + charPrefixHashMap);
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < origData.length; i++) {
            char c = (char)origData[i];
            s.append(charPrefixHashMap.get(c));
        }
        length = s.length();
        System.out.println("Encoded string: " + s.toString());
        charPrefixHashMap.clear();

        StringBuffer tmp = new StringBuffer();
        for (byte b : origData)
            tmp.append(table.get(b).code);
        length = tmp.length();
        List<Byte> bytes = new ArrayList<Byte>();
        while (tmp.length() > 0) {
            while (tmp.length() < 8)
                tmp.append('0');
            String str = tmp.substring(0, 8);
            bytes.add((byte) Integer.parseInt(str, 2));
            tmp.delete(0, 8);
        }
        byte[] ret = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++)
            ret[i] = bytes.get(i);
        return ret;
    }

    /** Decoding the byte array using this prefixcode.
     * @param encodedData encoded data
     * @return decoded data (hopefully identical to original)
     */
    public byte[] decode (byte[] encodedData) {

        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < encodedData.length; i++)
            tmp.append(String.format("%8s", Integer.toBinaryString(encodedData[i] & 0xFF)).replace(' ', '0'));
        String str = tmp.substring(0, length);
        List<Byte> bytes = new ArrayList<Byte>();
        String code = "";
        while (str.length() > 0) {
            code += str.substring(0, 1);
            str = str.substring(1);

            Iterator<Leaf> list = table.values().iterator();
            while (list.hasNext()) {
                Leaf leaf = list.next();
                if (leaf.code.equals(code)) {
                    bytes.add(leaf.value);
                    code = "";
                    break;
                }
            }
        }
        byte[] ret = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            ret[i] = bytes.get(i);
        }
        String decodedData = new String(ret);  // Create new String Object and assign byte[] to it
        System.out.println("Decoded string: " + decodedData);

        return ret;
    }


    /** Main method. */
    public static void main (String[] params) {
        String tekst = "AABBCCCC";
        byte[] orig = tekst.getBytes();
        Huffman huf = new Huffman (orig);
        byte[] kood = huf.encode (orig);
        byte[] orig2 = huf.decode (kood);
        // must be equal: orig, orig2
        System.out.println (Arrays.equals (orig, orig2));
        int lngth = huf.bitLength();
        System.out.println ("Length of encoded data in bits: " + lngth);
        // TODO!!! Your tests here!
    }
}

