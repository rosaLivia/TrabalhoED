package trabalhoED.Compactacao;

public class TrieNode implements Comparable<TrieNode>{
	char character; // Caractere armazenado no nó
	int frequency; // Frequência do caractere
	TrieNode left, right; // Filhos à esquerda e à direita
	
	// Construtor para o nó intermediário
	public TrieNode(int frequency, TrieNode left, TrieNode right) { 
		this.character = '\0'; // Não tem caractere
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}
	
	// Construtor para o nó folha
	public TrieNode(char character, int frequency) { 
		this.character = character; 
		this.frequency = frequency;
		this.left = null;
		this.right = null;
	}

	@Override
	public int compareTo(TrieNode otherNode) {
		return this.frequency - otherNode.frequency;
	}
}