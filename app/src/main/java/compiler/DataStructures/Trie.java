package compiler.DataStructures;
import java.util.AbstractMap.SimpleEntry;

public class Trie<T> {
    public static class NoTransitionException extends Exception { 
        public NoTransitionException(char c) { super("No path for: " + c); }
    }

    private TrieNode<T> rootNode;

    public Trie(Iterable<SimpleEntry<String, T>> valuesByWord) {
        this.rootNode = new TrieNode<T>('-', null);
        for(SimpleEntry<String, T> word : valuesByWord) 
        {
            this.insert(word);
        };
    };

    public void insert(SimpleEntry<String, T> valueByWord) {
        String word = valueByWord.getKey();
        T value = valueByWord.getValue();
        TrieNode<T> currNode = this.rootNode;
        for(int i = 0; i < word.length(); i++) 
        {
            Character currChar = word.charAt(i);
            currNode = currNode.tryAttachAndGetChild(currChar, null);
        };
        currNode.tryAttach('*', value);
    };

    public T getWord(String word) 
    {
        TrieNode<T> currNode = this.rootNode;
        for(int i = 0; i < word.length(); i++) 
        {
            Character currChar = word.charAt(i);
            if(!currNode.hasChild(currChar)) return null;
            currNode = currNode.getChild(currChar);
        };

        if(!currNode.hasChild('*')) return null;

        return currNode.getChild('*').value;
    };

    public boolean containsWord(String word) 
    {
        return getWord(word) != null;
    };

    public TrieNavigator getNavigator() {
        return new TrieNavigator();
    };

    public class TrieNavigator {
        private TrieNode<T> currNode = rootNode;
        private int longestMatchedSoFarLength = 0;
        private T longestMatchedSoFar = null;
        private int readChars = 0;

        public void nextChar(char currChar) throws NoTransitionException
        {
            if(!rootNode.hasChild(currChar)) throw new NoTransitionException(currChar);
            this.currNode = currNode.getChild(currChar);
            readChars += 1;
            if(this.currNode.hasChild('*')) 
            {
                this.longestMatchedSoFar = this.currNode.getChild('*').value;
                this.longestMatchedSoFarLength = readChars;
            };
        };

        public T matchedWord() 
        {
            if(!currNode.hasChild('*')) return null;
            return currNode.getChild('*').value;
        }; 

        public T getLongestMatchedSoFar() 
        {
            return this.longestMatchedSoFar;
        };

        public int getLongestMatchedSoFarLength() 
        {
            return this.longestMatchedSoFarLength;
        };
    }
}
