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

    // Método para construir a árvore de Huffman
    private void construirArvore(String texto) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (char c : texto.toCharArray()) {
            frequencias.put(c, frequencias.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<NoHuffman> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencias.entrySet()) {
            pq.add(new NoHuffman(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            NoHuffman esquerda = pq.poll();
            NoHuffman direita = pq.poll();
            NoHuffman pai = new NoHuffman('\0', esquerda.frequencia + direita.frequencia);
            pai.esquerda = esquerda;
            pai.direita = direita;
            pq.add(pai);
        }

        raiz = pq.poll();
    }

    // Método para comprimir o texto
    public byte[] comprimir(String texto) {
        construirArvore(texto);
        gerarCodigos(raiz, "");

        StringBuilder textoCodificado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            textoCodificado.append(tabelaHuffman.get(c));
        }

        // Convertendo a sequência de bits em array de bytes
        BitSet bits = new BitSet(textoCodificado.length());
        for (int i = 0; i < textoCodificado.length(); i++) {
            if (textoCodificado.charAt(i) == '1') {
                bits.set(i);
            }
        }

        return bits.toByteArray();
    }

    // Método para descomprimir o texto
    public String descomprimir(byte[] bytes) {
        try {

            // Convertendo array de bytes em sequência de bits
            BitSet bits = BitSet.valueOf(bytes);
            StringBuilder textoCodificado = new StringBuilder();
            for (int i = 0; i < bits.length(); i++) {
                textoCodificado.append(bits.get(i) ? '1' : '0');
            }

            // Decodificando a sequência de bits usando a árvore de Huffman
            StringBuilder textoDescomprimido = new StringBuilder();
            NoHuffman atual = raiz;
            for (char bit : textoCodificado.toString().toCharArray()) {
                if (bit == '0') {
                    atual = atual.esquerda;
                } else {
                    atual = atual.direita;
                }

                if (atual.esquerda == null && atual.direita == null) {
                    textoDescomprimido.append(atual.caractere);
                    atual = raiz;
                }
            }

            return textoDescomprimido.toString();
        } catch (Exception e) {
            System.out.println("Erro durante a descompressão: " + e.getMessage());
            return "";
        }
    }

    // Método para salvar a árvore de Huffman
    public void salvarArvore(String caminhoArquivo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            salvarNo(out, raiz);
        }
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

    // Método para carregar a árvore de Huffman
    public void carregarArvore(String caminhoArquivo) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {
            raiz = carregarNo(in);
        }
    }

    private NoHuffman carregarNo(ObjectInputStream in) throws IOException {
        try {
            if (!in.readBoolean()) {
                return null;
            }
            char caractere = in.readChar();
            int frequencia = in.readInt();
            NoHuffman no = new NoHuffman(caractere, frequencia);
            no.esquerda = carregarNo(in);
            no.direita = carregarNo(in);
            return no;
        } catch (EOFException e) {
            return null;
        }
    }

    private void gerarCodigos(NoHuffman no, String codigo) {
        if (no != null) {
            if (no.caractere != '\0') {
                tabelaHuffman.put(no.caractere, codigo);
            }
            gerarCodigos(no.esquerda, codigo + "0");
            gerarCodigos(no.direita, codigo + "1");
        }
    }

}
