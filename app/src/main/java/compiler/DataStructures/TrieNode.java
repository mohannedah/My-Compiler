package compiler.DataStructures;

public class TrieNode<T> {
    public Character trieChar;
    public T value;
    public TrieNode<T>[] children;

    public int getIndexCode(Character trieChar) 
    {
        return (int)trieChar;
    };

    @SuppressWarnings("unchecked")
    public TrieNode(Character trieChar, T value) 
    {
        this.trieChar = trieChar;
        this.value = value;
        this.children = (TrieNode<T>[]) new TrieNode[(1 << 7)];
    };

    public TrieNode<T> getChild(Character child) 
    {
        int charIndex = this.getIndexCode(child);
        return this.children[charIndex];
    };

    public boolean hasChild(Character child) 
    { 
        return getChild(child) != null;
    };

    public void attach(TrieNode<T> node) 
    {
        int charIndex = this.getIndexCode(node.trieChar);
        this.children[charIndex] = node;
    };

    public boolean tryAttach(Character child, T value) 
    {
        if(hasChild(child)) return false;

        TrieNode<T> newNode = new TrieNode<T>(child, value);
        attach(newNode);
     
        return true;
    };

    public TrieNode<T> tryAttachAndGetChild(Character child, T value) 
    {
        tryAttach(child, value);
        return getChild(child);
    };
}
