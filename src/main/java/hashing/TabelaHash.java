package main.java.hashing;

import java.util.Objects;

public class TabelaHash<K, V extends TabelaHash.ValorComArvore<byte[]>> {
    private static final int TAMANHO_INICIAL = 16;
    private static final double FATOR_CARGA = 0.75;

    private EntradaHash<K, V>[] tabela;
    private int tamanho;
    private int capacidade;
    private String tipoFuncaoHash;

    // Construtor que define a função de hash (divisao ou DJB2)
    @SuppressWarnings("unchecked")
    public TabelaHash(String tipoFuncaoHash) {
        this.capacidade = TAMANHO_INICIAL;
        this.tabela = (EntradaHash<K, V>[]) new EntradaHash[capacidade];
        this.tipoFuncaoHash = tipoFuncaoHash.toLowerCase();
        this.tamanho = 0;
    }

    // Classe interna para os pares chave-valor
    private static class EntradaHash<K, V> {
        K chave;
        V valor;
        EntradaHash<K, V> proximo;

        public EntradaHash(K chave, V valor, EntradaHash<K, V> proximo) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = proximo;
        }
    }

    // Classe interna para armazenar valor e caminho da árvore (Genérica)
    public static class ValorComArvore<T> {
        private T valor;
        private String caminhoArvore;

        public ValorComArvore(T valor, String caminhoArvore) {
            this.valor = valor;
            this.caminhoArvore = caminhoArvore;
        }

        public T getValor() {
            return valor;
        }

        public String getCaminhoArvore() {
            return caminhoArvore;
        }
    }

    // Método para inserir um elemento na tabela hash
    public void inserir(K chave, byte[] valor, String caminhoArvore) {
        int indice = calcularIndice(chave);
        EntradaHash<K, V> atual = tabela[indice];

        // Verifica se a chave já existe
        while (atual != null) {
            if (Objects.equals(atual.chave, chave)) {
                // Atualiza o valor existente
                atual.valor = (V) new ValorComArvore<byte[]>(valor, caminhoArvore);
                return;
            }
            atual = atual.proximo;
        }

        // Cria uma nova entrada
        V valorComArvore = (V) new ValorComArvore<byte[]>(valor, caminhoArvore);
        EntradaHash<K, V> novaEntrada = new EntradaHash<>(chave, valorComArvore, tabela[indice]);
        tabela[indice] = novaEntrada;
        tamanho++;

        // Redimensiona a tabela se necessário
        if ((1.0 * tamanho) / capacidade >= FATOR_CARGA) {
            redimensionarTabela();
        }
    }

    // Método para buscar com retorno do caminho da árvore
    public ValorComArvore<byte[]> buscarComArvore(K chave) {
        int indice = calcularIndice(chave);
        EntradaHash<K, V> atual = tabela[indice];

        while (atual != null) {
            if (Objects.equals(atual.chave, chave)) {
                return atual.valor;
            }
            atual = atual.proximo;
        }
        return null; // Retorna null se não encontrar
    }

    // Método para calcular o índice baseado na chave e na função de hash escolhida
    private int calcularIndice(K chave) {
        int hash;
        if (tipoFuncaoHash.equals("divisao")) {
            hash = hashDivisao(chave.toString(), capacidade);
        } else {
            hash = hashDJB2(chave.toString());
        }
        return Math.abs(hash) % capacidade;
    }

    // Função de hash por Divisão conforme especificado
    public int hashDivisao(String texto, int M) {
        int soma = 0;
        for (char c : texto.toCharArray()) {
            soma += c;
        }
        return soma % M;
    }

    // Função de hash DJB2 atualizada
    private int hashDJB2(String texto) {
        int hash = 5381;
        for (char c : texto.toCharArray()) {
            hash = ((hash << 5) + hash) + c; // hash * 33 + c
        }
        return hash;
    }

    // Método para redimensionar a tabela quando o fator de carga é excedido
    @SuppressWarnings("unchecked")
    private void redimensionarTabela() {
        capacidade *= 2;
        EntradaHash<K, V>[] novaTabela = (EntradaHash<K, V>[]) new EntradaHash[capacidade];
        for (EntradaHash<K, V> entrada : tabela) {
            while (entrada != null) {
                EntradaHash<K, V> proximo = entrada.proximo;
                int indice = calcularIndice(entrada.chave);
                entrada.proximo = novaTabela[indice];
                novaTabela[indice] = entrada;
                entrada = proximo;
            }
        }
        tabela = novaTabela;
    }

    // Métodos para obter o número de elementos e verificar se a tabela está vazia
    public int getTamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }
}