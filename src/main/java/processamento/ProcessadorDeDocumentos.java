package main.java.processamento;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ProcessadorDeDocumentos {
    private String conteudo;

    // Carrega um documento TXT cuidando do encoding UTF-8
    public void carregarDocumento(String caminhoArquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha).append("\n");
            }
        }
        conteudo = sb.toString();
    }