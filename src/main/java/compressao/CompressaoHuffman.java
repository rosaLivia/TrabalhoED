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

        // Construir a árvore
        while (fila.size() > 1) {
            NoHuffman no1 = fila.poll();
            NoHuffman no2 = fila.poll();

            NoHuffman novoNo = new NoHuffman('\0', no1.frequencia + no2.frequencia);
            novoNo.esquerda = no1;
            novoNo.direita = no2;

            fila.add(novoNo);
        }

        raiz = fila.poll();

        // Gerar códigos de Huffman
        gerarCodigos(raiz, "");
    }

    // Método recursivo para gerar os códigos de Huffman para cada caractere
    private void gerarCodigos(NoHuffman no, String codigo) {
        if (no != null) {
            // Se o nó é uma folha
            if (no.esquerda == null && no.direita == null) {
                tabelaHuffman.put(no.caractere, codigo);
            }
            gerarCodigos(no.esquerda, codigo + "0");
            gerarCodigos(no.direita, codigo + "1");
        }
    }

}
