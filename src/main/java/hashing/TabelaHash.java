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

    // Remove um elemento pela chave
    public void remover(K chave);

    // Função de hash por divisão
    private int funcaoHashDivisao(K chave);

    // Função de hash DJB2
    private int funcaoHashDJB2(K chave);
}