package main.java.compressao;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

public class NoHuffman implements Comparable<NoHuffman> {

    char caractere;
    int frequencia;
    NoHuffman esquerda;
    NoHuffman direita;

    public NoHuffman(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esquerda = null;
        this.direita = null;
    }

    @Override
    public int compareTo(NoHuffman outro) {
        return this.frequencia - outro.frequencia;
    }

}