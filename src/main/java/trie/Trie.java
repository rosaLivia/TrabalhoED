package main.java.trie;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode raiz;

    public Trie() {
        this.raiz = new TrieNode();
    }

    // Normaliza a palavra (remove acentuação e converte para minúsculas)
    private String normalizarPalavra(String palavra) {
        palavra = palavra.toLowerCase();
        palavra = Normalizer.normalize(palavra, Normalizer.Form.NFD);
        palavra = palavra.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return palavra;
    }