package main;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.List;
import main.java.hashing.TabelaHash;
import main.java.trie.Trie;
import main.java.compressao.CompressaoHuffman;
import main.java.processamento.ProcessadorDeDocumentos;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String diretorioDocumentos;
        String tipoFuncaoHash;
        TabelaHash<String, TabelaHash.ValorComArvore<byte[]>> tabelaHash;
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
        scanner.nextLine(); 

        if (opcaoHash == 1) {
            tipoFuncaoHash = "divisao";
        } else {
            tipoFuncaoHash = "djb2";
        }

        tabelaHash = new TabelaHash<>(tipoFuncaoHash);

        // Medindo tempo de indexação
        long inicioIndexacao = System.currentTimeMillis();
        long memoriaInicialIndexacao = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Processando os documentos
        File pasta = new File(diretorioDocumentos);
        File[] listaArquivos = pasta.listFiles((dir, name) -> name.endsWith(".txt"));

        if (listaArquivos != null && listaArquivos.length > 0) {
            for (File arquivo : listaArquivos) {
                try {
                    ProcessadorDeDocumentos processador = new ProcessadorDeDocumentos();
                    processador.carregarDocumento(arquivo.getAbsolutePath());
                    List<String> palavras = processador.processarDocumento();

                    String caminhoArvore = arquivo.getAbsolutePath() + ".huff";

                    // Comprimindo o conteúdo e salvando a árvore
                    byte[] conteudoComprimido = compressao.comprimir(String.join(" ", palavras));
                    compressao.salvarArvore(caminhoArvore);

                    // Inseririndo na tabela hash
                    tabelaHash.inserir(arquivo.getName(), conteudoComprimido, caminhoArvore);

                    // Inseririndo palavras na trie
                    for (String palavra : palavras) {
                        trie.inserir(palavra, arquivo.getName());
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao processar o arquivo: " + arquivo.getName());
                }
            }
        } else {
            System.out.println("Diretório inválido ou nenhum arquivo .txt encontrado.");
            scanner.close();
            return;
        }

        long fimIndexacao = System.currentTimeMillis();
        long memoriaFinalIndexacao = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Tempo de indexação: " + (fimIndexacao - inicioIndexacao) + " ms");
        System.out.println(
                "Consumo de memória na indexação: " + (memoriaFinalIndexacao - memoriaInicialIndexacao) / (1024 * 1024)
                        + " MB");

        while (true) {
            System.out.println("\nDigite a palavra a ser pesquisada (ou 'sair' para encerrar):");
            String palavraBusca = scanner.nextLine();
            if (palavraBusca.equalsIgnoreCase("sair")) {
                break;
            }

            // Medindo tempo de busca
            long inicioBusca = System.currentTimeMillis();
            long memoriaInicialBusca = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Buscando nos documentos
            var documentosEncontrados = trie.buscar(palavraBusca);
            if (documentosEncontrados.isEmpty()) {
                System.out.println("Palavra não encontrada nos documentos.");
            } else {
                System.out.println("A palavra '" + palavraBusca + "' foi encontrada nos seguintes documentos:");
                int index = 1;
                for (String doc : documentosEncontrados) {
                    System.out.println(index + ". " + doc);
                    index++;
                }

                // Opção para abrir e ler o conteúdo de um documento
                System.out.print("Deseja abrir algum documento? (s/n): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("s")) {
                    System.out.print("Digite o número do documento que deseja abrir: ");
                    try {
                        int escolha = Integer.parseInt(scanner.nextLine());
                        if (escolha >= 1 && escolha <= documentosEncontrados.size()) {
                            String documentoSelecionado = documentosEncontrados.get(escolha - 1);
                            TabelaHash.ValorComArvore<byte[]> valorComArvore = tabelaHash
                                    .buscarComArvore(documentoSelecionado);
                            if (valorComArvore != null) {
                                // Carregando a árvore de Huffman correspondente
                                compressao.carregarArvore(valorComArvore.getCaminhoArvore());

                                String conteudoDescomprimido = compressao.descomprimir(valorComArvore.getValor());
                                System.out.println("\n--- Conteúdo do Documento: " + documentoSelecionado + " ---");
                                System.out.println(conteudoDescomprimido);
                                System.out.println("--- Fim do Documento ---\n");
                            } else {
                                System.out.println("Erro: Documento não encontrado na tabela hash.");
                            }
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida.");
                    } catch (IOException e) {
                        System.out.println("Erro ao carregar a árvore de Huffman: " + e.getMessage());
                    }
                }
            }

            long fimBusca = System.currentTimeMillis();
            long memoriaFinalBusca = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Tempo de busca: " + (fimBusca - inicioBusca) + " ms");
            System.out.println("Consumo de memória na busca: "
                    + (memoriaFinalBusca - memoriaInicialBusca) / (1024 * 1024) + " MB");
        }

        scanner.close();
        System.out.println("Encerrando o sistema.");
    }
}