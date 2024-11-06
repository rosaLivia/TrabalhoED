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

    // Insere um elemento na tabela hash
    public void inserir(K chave, V valor);

    // Busca um elemento pela chave
    public V buscar(K chave);

    // Remove um elemento pela chave
    public void remover(K chave);

    // Função de hash por divisão
    private int funcaoHashDivisao(K chave);

    // Função de hash DJB2
    private int funcaoHashDJB2(K chave);
}