package main.java.trie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrieNode {
    private Map<Character, TrieNode> filhos;
    private boolean fimDaPalavra;
    private Set<String> documentos;

    public TrieNode() {
        this.filhos = new HashMap<>();
        this.fimDaPalavra = false;
        this.documentos = new HashSet<>();
    }

    public Map<Character, TrieNode> getFilhos() {
        return filhos;
    }

    public boolean isFimDaPalavra() {
        return fimDaPalavra;
    }

    public void setFimDaPalavra(boolean fimDaPalavra) {
        this.fimDaPalavra = fimDaPalavra;
    }

    public Set<String> getDocumentos() {
        return documentos;
    }
}