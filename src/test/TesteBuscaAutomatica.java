package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import main.java.hashing.TabelaHash;
import main.java.trie.Trie;
import main.java.compressao.CompressaoHuffman;
import main.java.processamento.ProcessadorDeDocumentos;

public class TesteBuscaAutomatica {
    public static void main(String[] args) {
        String diretorioDocumentos;
        String caminhoQueries;

        // Verificar se os argumentos foram fornecidos
        if (args.length >= 2) {
            diretorioDocumentos = args[0];
            caminhoQueries = args[1];
        } else {
            // Solicitar ao usuário os caminhos
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite o caminho para o diretório dos documentos: ");
            diretorioDocumentos = scanner.nextLine().trim();
            System.out.print("Digite o caminho para o arquivo de queries (queries.txt): ");
            caminhoQueries = scanner.nextLine().trim();
            scanner.close();
        }

        String tipoFuncaoHash = "djb2"; // ou "divisao" conforme preferir
        TabelaHash<String, TabelaHash.ValorComArvore<byte[]>> tabelaHash = new TabelaHash<>(tipoFuncaoHash);
        Trie trie = new Trie();
        CompressaoHuffman compressao = new CompressaoHuffman();

        // Inicializando e indexando documentos
        inicializarIndexacao(diretorioDocumentos, tabelaHash, trie, compressao);

        // Executando testes de busca
        executarTestes(caminhoQueries, tabelaHash, trie);
    }

    private static void inicializarIndexacao(String diretorioDocumentos,
            TabelaHash<String, TabelaHash.ValorComArvore<byte[]>> tabelaHash, Trie trie, CompressaoHuffman compressao) {
        ProcessadorDeDocumentos processador = new ProcessadorDeDocumentos();
        File pasta = new File(diretorioDocumentos);
        File[] listaArquivos = pasta.listFiles((dir, name) -> name.endsWith(".txt"));

        if (listaArquivos != null && listaArquivos.length > 0) {
            for (File arquivo : listaArquivos) {
                try {
                    processador.carregarDocumento(arquivo.getAbsolutePath());
                    List<String> palavras = processador.processarDocumento();

                    String caminhoArvore = arquivo.getAbsolutePath() + ".huff";
                    byte[] conteudoComprimido = compressao.comprimir(String.join(" ", palavras));
                    compressao.salvarArvore(caminhoArvore);

                    tabelaHash.inserir(arquivo.getName(), conteudoComprimido, caminhoArvore);

                    for (String palavra : palavras) {
                        trie.inserir(palavra, arquivo.getName());
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao processar o arquivo: " + arquivo.getName());
                }
            }
        } else {
            System.out.println("Diretório inválido ou nenhum arquivo .txt encontrado em: " + diretorioDocumentos);
        }
    }

    private static void executarTestes(String caminhoQueries,
            TabelaHash<String, TabelaHash.ValorComArvore<byte[]>> tabelaHash, Trie trie) {
        int totalQueries = 0;
        int queriesEncontradas = 0;
        int queriesNaoEncontradas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoQueries))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                totalQueries++;
                String query = linha.trim().toLowerCase();

                long inicioBusca = System.currentTimeMillis();
                List<String> documentos = trie.buscar(query);
                long fimBusca = System.currentTimeMillis();

                if (!documentos.isEmpty()) {
                    queriesEncontradas++;
                    System.out.println("[" + totalQueries + "] " + query + ": ENCONTRADA em " + documentos.size()
                            + " documentos. Tempo: " + (fimBusca - inicioBusca) + " ms");
                } else {
                    queriesNaoEncontradas++;
                    System.out.println("[" + totalQueries + "] " + query + ": NÃO ENCONTRADA. Tempo: "
                            + (fimBusca - inicioBusca) + " ms");
                }
            }

            // Resumo dos Testes
            System.out.println("\n===== Resumo dos Testes de Busca =====");
            System.out.println("Total de Consultas: " + totalQueries);
            System.out.println("Consultas Encontradas: " + queriesEncontradas);
            System.out.println("Consultas Não Encontradas: " + queriesNaoEncontradas);
            System.out.println("Taxa de Sucesso: " + ((queriesEncontradas * 100.0) / totalQueries) + "%");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de consultas: " + e.getMessage());
        }
    }
}