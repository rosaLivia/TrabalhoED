package trie;

import trabalhoED.Compactacao.TrieNode;

public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Método para inserir uma palavra na Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }
        node.isEndOfWord = true;
    }

    // Método para buscar uma palavra completa na Trie
    public boolean search(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }

    // Método para verificar se existe alguma palavra que começa com o prefixo
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                return false;
            }
        }
        return true;
    }
}