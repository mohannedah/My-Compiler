package compiler.DataStructures;
import java.util.AbstractMap.SimpleEntry;

public class Trie<T> {
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
}
