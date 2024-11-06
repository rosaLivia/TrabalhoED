package main.java.hashing;

public class TabelaHash<K, V> {

    private static final int TAMANHO_INICIAL = 16;
    private static final double FATOR_CARGA = 0.75;

    private EntradaHash<K, V>[] tabela;
    private int tamanho;
    private int capacidade;
    private String tipoFuncaoHash;

    // Construtor que define a função de hash (divisão ou DJB2)
    public TabelaHash(String tipoFuncaoHash) {
        this.capacidade = TAMANHO_INICIAL;
        this.tabela = new EntradaHash[capacidade];
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

    // Método para inserir um elemento na tabela hash
    public void inserir(K chave, V valor) {
        int indice = calcularIndice(chave);
        EntradaHash<K, V> novaEntrada = new EntradaHash<>(chave, valor, null);

        if (tabela[indice] == null) {
            tabela[indice] = novaEntrada;
        } else {
            EntradaHash<K, V> atual = tabela[indice];
            while (atual != null) {
                if (atual.chave.equals(chave)) {
                    // Atualiza o valor se a chave já existir
                    atual.valor = valor;
                    return;
                }
                if (atual.proximo == null) {
                    atual.proximo = novaEntrada;
                    break;
                }
                atual = atual.proximo;
            }
        }
        tamanho++;

        // Redimensiona a tabela se necessário
        if ((1.0 * tamanho) / capacidade >= FATOR_CARGA) {
            redimensionarTabela();
        }
    }

    // Método para buscar um elemento pela chave
    public V buscar(K chave) {
        int indice = calcularIndice(chave);
        EntradaHash<K, V> atual = tabela[indice];

        while (atual != null) {
            if (atual.chave.equals(chave)) {
                return atual.valor;
            }
            atual = atual.proximo;
        }
        return null; // Retorna null se não encontrar
    }

    // Método para remover um elemento pela chave
    public void remover(K chave) {
        int indice = calcularIndice(chave);
        EntradaHash<K, V> atual = tabela[indice];
        EntradaHash<K, V> anterior = null;

        while (atual != null) {
            if (atual.chave.equals(chave)) {
                if (anterior == null) {
                    tabela[indice] = atual.proximo;
                } else {
                    anterior.proximo = atual.proximo;
                }
                tamanho--;
                return;
            }
            anterior = atual;
            atual = atual.proximo;
        }
    }

    // Método para calcular o índice baseado na chave e na função de hash escolhida
    private int calcularIndice(K chave) {
        int hashCode = 0;
        if (tipoFuncaoHash.equals("divisao")) {
            hashCode = funcaoHashDivisao(chave);
        } else if (tipoFuncaoHash.equals("djb2")) {
            hashCode = funcaoHashDJB2(chave);
        }
        return Math.abs(hashCode) % capacidade;
    }

    // Função de hash DJB2
    private int funcaoHashDJB2(K chave) {
        String str = chave.toString();
        int hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        return hash;
    }

    // Função de hash por divisão
    private int funcaoHashDivisao(K chave) {
        return chave.hashCode();
    }

    // Método para redimensionar a tabela quando o fator de carga é excedido
    private void redimensionarTabela() {
        capacidade *= 2;
        EntradaHash<K, V>[] tabelaAntiga = tabela;
        tabela = new EntradaHash[capacidade];
        tamanho = 0;

        for (EntradaHash<K, V> entrada : tabelaAntiga) {
            while (entrada != null) {
                inserir(entrada.chave, entrada.valor);
                entrada = entrada.proximo;
            }
        }
    }

    // Método para obter o número de elementos na tabela
    public int getTamanho() {
        return tamanho;
    }

    // Método para verificar se a tabela está vazia
    public boolean estaVazia() {
        return tamanho == 0;
    }
}