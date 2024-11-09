## Descrição dos diretórios e arquivos - Temporaria:

`````bash
    src/main/java/: Contém o código-fonte principal.
    compressao/CompressaoHuffman.java: Classe para compressão e descompressão usando Huffman.
    hashing/: Contém classes relacionadas à tabela hash.
    HashEntry.java: Classe para entradas da tabela hash.
    TabelaHash.java: Classe para a tabela hash.
    processamento/ProcessadorDeDocumentos.java: Classe para processar documentos TXT.
    trie/: Contém classes relacionadas à estrutura Trie.
    Trie.java: Classe para a Trie.
    TrieNode.java: Classe para os nós da Trie.
    src/main/resources/documentos/: Contém documentos de exemplo para processamento.

Compilação de packages e execução do código
É necessário navegar até o diretório onde está contido o arquivo src e então digitar o seguinte comando:
    ```bash
        javac main/java/hashing/TabelaHash.java main/java/trie/Trie.java main/java/compressao/CompressaoHuffman.java main/java/compressao/NoHuffman.java main/java/processamento/ProcessadorDeDocumentos.java main/Main.java

        executar código: java main.Main

    ````
`````
