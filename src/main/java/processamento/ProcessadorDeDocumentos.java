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

    // Processa o documento carregado: limpa, normaliza e separa em palavras
    public List<String> processarDocumento() {
        if (conteudo == null || conteudo.isEmpty()) {
            return new ArrayList<>();
        }

        // Normalizar texto: remover acentos e caracteres especiais
        String textoNormalizado = Normalizer.normalize(conteudo, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase();

        // Remover caracteres não alfanuméricos
        textoNormalizado = textoNormalizado.replaceAll("[^\\p{L}\\p{N}\\s]", " ");

        // Dividir em palavras usando espaço como delimitador
        String[] palavrasArray = textoNormalizado.split("\\s+");

        List<String> palavras = new ArrayList<>();
        for (String palavra : palavrasArray) {
            if (!palavra.isEmpty()) {
                palavras.add(palavra);
            }
        }

        return palavras;
    }
    public String getConteudo() {
        return conteudo;
    }
}
