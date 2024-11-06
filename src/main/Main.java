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

        System.out.println("=== Sistema de Indexação de Textos ===");
        System.out.print("Digite o diretório dos documentos: ");
        diretorioDocumentos = scanner.nextLine();

        System.out.println("Escolha a função de hash:");
        System.out.println("1 - Divisão");
        System.out.println("2 - DJB2");
        System.out.print("Opção: ");
        int opcaoHash = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        if (opcaoHash == 1) {
            tipoFuncaoHash = "divisao";
        } else {
            tipoFuncaoHash = "djb2";
        }

        tabelaHash = new TabelaHash<>(tipoFuncaoHash);

        // Processar os documentos
        File pasta = new File(diretorioDocumentos);
        File[] listaArquivos = pasta.listFiles((dir, name) -> name.endsWith(".txt"));
    }
}