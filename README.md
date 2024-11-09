## Descrição dos Diretórios e Arquivos

- `src/main/java/`: Contém o código-fonte principal.
  - `compressao/CompressaoHuffman.java`: Classe para compressão e descompressão usando o algoritmo de Huffman.
  - `hashing/`: Contém classes relacionadas à tabela hash.
    - `HashEntry.java`: Classe para entradas da tabela hash.
    - `TabelaHash.java`: Classe para a tabela hash.
  - `processamento/ProcessadorDeDocumentos.java`: Classe para processar documentos TXT.
  - `trie/`: Contém classes relacionadas à estrutura Trie.
    - `Trie.java`: Classe para a Trie.
    - `TrieNode.java`: Classe para os nós da Trie.
- `src/main/resources/documentos/`: Contém documentos de exemplo para processamento.

## Compilação de Packages e Execução do Código

1. Navegue até o diretório que contém o diretório `src`.
2. Execute o comando abaixo para compilar todos os arquivos:

    ```bash
    javac main/java/hashing/TabelaHash.java main/java/trie/Trie.java main/java/compressao/CompressaoHuffman.java main/java/compressao/NoHuffman.java main/java/processamento/ProcessadorDeDocumentos.java main/Main.java
    ```

3. Após a compilação, execute o código com o seguinte comando:

    ```bash
    java main.Main
    ```

