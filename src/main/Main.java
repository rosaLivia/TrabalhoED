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
        if (listaArquivos != null && listaArquivos.length > 0) {
            for (File arquivo : listaArquivos) {
                try {
                    // Carregar o conteúdo do arquivo
                    ProcessadorDeDocumentos processador = new ProcessadorDeDocumentos();
                    String conteudo = processador.carregarDocumento(arquivo.getAbsolutePath());

                    // Comprimir o conteúdo
                    byte[] conteudoComprimido = compressao.comprimir(conteudo);

                    // Inserir na tabela hash
                    tabelaHash.inserir(arquivo.getName(), conteudoComprimido);

                    // Inserir palavras na trie
                    String[] palavras = conteudo.split("\\W+");
                    for (String palavra : palavras) {
                        if (!palavra.isEmpty()) {
                            trie.inserir(palavra, arquivo.getName());
                        }
                    }

                    System.out.println("Processado: " + arquivo.getName());
                } catch (IOException e) {
                    System.out.println("Erro ao processar o arquivo: " + arquivo.getName());
                }
            }
        } else {
            System.out.println("Diretório inválido ou nenhum arquivo .txt encontrado.");
            scanner.close();
            return;
        }
    }
}