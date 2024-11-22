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
        palavra = palavra.replaceAll("[^a-z0-9]", ""); // Remove não alfanuméricos
        return palavra;
    }

    // Insere uma palavra na Trie, associando-a a um documento
    public void inserir(String palavra, String documentoId) {
        palavra = normalizarPalavra(palavra);
        TrieNode noAtual = raiz;

        for (char caractere : palavra.toCharArray()) {
            noAtual = noAtual.getFilhos().computeIfAbsent(caractere, c -> new TrieNode());
        }
        noAtual.setFimDaPalavra(true);
        noAtual.getDocumentos().add(documentoId);
    }

    // Busca uma palavra na Trie e retorna a lista de documentos onde aparece
    public List<String> buscar(String palavra) {
        palavra = normalizarPalavra(palavra);
        TrieNode noAtual = raiz;

        for (char caractere : palavra.toCharArray()) {
            noAtual = noAtual.getFilhos().get(caractere);
            if (noAtual == null) {
                return new ArrayList<>(); // Palavra não encontrada
            }
        }

        if (noAtual.isFimDaPalavra()) {
            return new ArrayList<>(noAtual.getDocumentos());
        } else {
            return new ArrayList<>();
        }
    }
}