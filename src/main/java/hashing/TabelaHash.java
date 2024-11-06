package main.java.hashing;

public class TabelaHash<K, V> {

    // Construtor que define o tamanho e a função de hash (divisão ou DJB2)
    public TabelaHash(int tamanho, String tipoFuncaoHash);

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