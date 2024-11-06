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

    // Método para comprimir o texto
    public byte[] comprimir(String texto) {
        // Construir a árvore e gerar a tabela
        construirArvore(texto);

        // Codificar o texto
        StringBuilder textoCodificado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            textoCodificado.append(tabelaHuffman.get(c));
        }

        // Converter a sequência de bits em um array de bytes
        int comprimento = textoCodificado.length();
        BitSet bits = new BitSet(comprimento);
        for (int i = 0; i < comprimento; i++) {
            if (textoCodificado.charAt(i) == '1') {
                bits.set(i);
            }
        }

        // Converter BitSet em array de bytes
        byte[] bytes = bits.toByteArray();

        return bytes;
    }

    // Método para descomprimir o texto
    public String descomprimir(byte[] bytes) {
        // Converter array de bytes em sequência de bits
        BitSet bits = BitSet.valueOf(bytes);
        StringBuilder textoDescomprimido = new StringBuilder();

        NoHuffman noAtual = raiz;
        for (int i = 0; i <= bits.length(); i++) {
            boolean bit = bits.get(i);
            if (bit) {
                noAtual = noAtual.direita;
            } else {
                noAtual = noAtual.esquerda;
            }

            // Se for um nó folha
            if (noAtual.esquerda == null && noAtual.direita == null) {
                textoDescomprimido.append(noAtual.caractere);
                noAtual = raiz;
            }
        }

        return textoDescomprimido.toString();
    }

    // Método para salvar a árvore de Huffman em um arquivo
    public void salvarArvore(ObjectOutputStream out) throws IOException {
        salvarNo(out, raiz);
    }

    private void salvarNo(ObjectOutputStream out, NoHuffman no) throws IOException {
        if (no == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        out.writeChar(no.caractere);
        out.writeInt(no.frequencia);
        salvarNo(out, no.esquerda);
        salvarNo(out, no.direita);
    }

    // Método para carregar a árvore de Huffman de um arquivo
    public void carregarArvore(ObjectInputStream in) throws IOException {
        raiz = carregarNo(in);
        tabelaHuffman = new HashMap<>();
        gerarCodigos(raiz, "");
    }

    private NoHuffman carregarNo(ObjectInputStream in) throws IOException {
        boolean temNo = in.readBoolean();
        if (!temNo) {
            return null;
        }
        char caractere = in.readChar();
        int frequencia = in.readInt();
        NoHuffman no = new NoHuffman(caractere, frequencia);
        no.esquerda = carregarNo(in);
        no.direita = carregarNo(in);
        return no;
    }

}
