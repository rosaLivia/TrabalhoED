package main;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import main.java.hashing.TabelaHash;
import main.java.trie.Trie;
import main.java.compressao.CompressaoHuffman;
import main.java.processamento.ProcessadorDeDocumentos;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String diretorioDocumentos;
        String tipoFuncaoHash;
        TabelaHash<String, byte[]> tabelaHash;
        Trie trie = new Trie();
        CompressaoHuffman compressao = new CompressaoHuffman();

    }
}
