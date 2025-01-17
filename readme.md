## Trabalho 2 - Sistemas Operacionais

#### Membros:
- João Ricardo Teixeira
- Rodrigo Soares
- Mariana Dutra
- Julia Vieira
- Natan Silva

### Implementar simulador de gerenciamento de arquivos com interpretador de comandos.

#### Requisitos:
- Simular diretórios (hierarquia) e aquivos (conteúdo).
- Apresentar erros para comandos inválidos ou não permitidos.
- Documentação do código.

#### Comandos implementados:

- `cat` `<arquivo>` <br> Mostrar o conteúdo de um arquivo.
- `cd` `..` <br> Voltar ao diretório anterior.
- `cd` `/` <br> Ir para o diretório raiz.
- `cd` `<nome>` <br> Navegar para um diretório específico.
- `chmod` `<permissao>` `<nome>` <br> Alterar permissões de um arquivo ou diretório (simular leitura, escrita e execução).
- `chown` `<proprietario>` `<nome>` <br> Alterar o proprietário de um arquivo ou diretório.
- `cp` `<origem>` `<destino>` <br> Copiar arquivos ou diretórios.
- `diff` `<arquivo1>` `<arquivo2>` <br> Comparar dois arquivos e exibir as diferenças.
- `du` `<diretorio>` <br> Exibir o tamanho do diretório em bytes.
- `echo` `<texto>` `>` `<arquivo>` <br> Adicionar ou sobrescrever o conteúdo de um arquivo.
- `echo` `<texto>` `>>` `<arquivo>` <br> Adicionar texto ao final do conteúdo existente de um arquivo.
- `find` `<diretorio>` `-name` `<nome>` <br> Procurar arquivos ou diretórios pelo nome em uma hierarquia.
- `grep` `<termo>` `<arquivo>` <br> Procurar por uma palavra ou frase dentro de um arquivo.
- `head` `<arquivo>` `<n>` <br> Exibir as primeiras `n` linhas do arquivo.
- `history` <br> Exibir os últimos comandos digitados
- `ls` `-l` <br> Listar conteúdo do diretório com detalhes (nome, tipo, tamanho, permissões, etc.).
- `mkdir` `<nome>` <br> Criar um novo diretório.
- `mv` `<origem>` `<destino>` <br> Mover arquivos ou diretórios para outra localização.
- `pwd` <br> Exibir o caminho completo do diretório atual.
- `rename` `<nome_antigo>` `<novo_nome>` <br> Renomear um arquivo ou diretório.
- `rm` `<nome>` <br> Remover um arquivo ou diretório (mesmo que não esteja vazio).
- `rmdir` `<nome>` <br> Remover um diretório vazio.
- `stat` `<nome>` <br> Exibir informações detalhadas de um arquivo ou diretório (tamanho, data de criação, última modificação, etc.).
- `tail` `<arquivo>` `<n>` <br> Exibir as últimas `n` linhas do arquivo.
- `touch` `<nome>` <br> Criar um arquivo vazio.
- `tree` <br> Mostrar a estrutura hierárquica de arquivos e diretórios.
- `unzip` `<arquivo.zip>` <br> Descompactar um arquivo `.zip`.
- `wc` `<arquivo>` <br> Mostrar o número de linhas, palavras e caracteres de um arquivo.
- `zip` `<arquivo.zip>` `<itens>` <br> Compactar arquivos ou diretórios em um arquivo `.zip` (simulado).
