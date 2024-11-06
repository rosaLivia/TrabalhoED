package main.java.compressao;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.BitSet;
import java.io.*;

public class CompressaoHuffman {
    private Map<Character, String> tabelaHuffman;
    private NoHuffman raiz;

    public CompressaoHuffman() {
        tabelaHuffman = new HashMap<>();
    }

    // Método para construir a árvore de Huffman e gerar a tabela de códigos
    private void construirArvore(String texto) {
        // Contar frequências
        Map<Character, Integer> frequencias = new HashMap<>();
        for (char c : texto.toCharArray()) {
            frequencias.put(c, frequencias.getOrDefault(c, 0) + 1);
        }

        // Criar fila de prioridade
        PriorityQueue<NoHuffman> fila = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
            fila.add(new NoHuffman(entrada.getKey(), entrada.getValue()));
        }

        
}
