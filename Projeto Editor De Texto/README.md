    # Editor de Texto Java-VI

Emulador simplificado do editor **VI** (Linux) que roda no console, escrito em Java. Foi feito como projeto da disciplina de programação do curso de Análise e Desenvolvimento de Sistemas na FATEC Bragança Paulista.

O VI é um editor *modal*: o que você digita depende do modo em que o editor está. Este projeto reproduz a ideia básica com três modos, um buffer em memória e gravação em arquivo texto.

## Sobre o uso de IA

Este projeto foi desenvolvido **com apoio de IA** (Claude, da Anthropic). Ela me ajudou a revisar o código, apontar erros e sugerir correções, como o tratamento de exceções na gravação e o comando `:x`. Não construí tudo sozinho, e prefiro deixar isso claro.

O que eu fiz: escrevi e testei o código, tomei as decisões de estrutura (por exemplo, só o modo de comando troca para os outros modos) e li o código até conseguir explicar o funcionamento com minhas palavras. Os testes manuais listados abaixo foram feitos por mim.

## Funcionalidades

- Pede o nome do arquivo ao iniciar. Se existir, carrega o conteúdo no buffer; se não existir, começa com buffer vazio e cria o arquivo ao salvar.
- Três modos de operação: **Comando**, **Inserção** e **Linha de Comando**.
- Inserir, editar e excluir linhas, com numeração começando em 1.
- Gravação em disco com tratamento de erros, sem fechar o editor se a gravação falhar.
- Aviso ao tentar sair com alterações não salvas.

## Comandos

**Modo de Comando** (modo inicial)

| Comando | Ação |
|---|---|
| `i` | Entra no modo de Inserção |
| `e` ou `e N` | Edita a linha N (pergunta o número se não for informado) |
| `d` ou `d N` | Exclui a linha N (pergunta o número se não for informado) |
| `:` | Entra no modo de Linha de Comando |

**Modo de Inserção**: cada linha digitada é adicionada ao final do buffer. Digite `ESC` ou `:ESC` para voltar ao modo de Comando.

**Modo de Linha de Comando**

| Comando | Ação |
|---|---|
| `:w` | Salva o buffer no arquivo |
| `:q` | Sai; se houver alterações não salvas, pede confirmação |
| `:wq` ou `:x` | Salva e sai |
| `:q!` | Sai sem salvar |

No modo de Comando, também é possível digitar o comando direto, por exemplo `:w`, sem passar antes pelo `:`.

## Como executar

Requisitos: Java 11 ou superior.

```bash
javac App.java
java App
```

Se os acentos aparecerem quebrados no terminal do Windows, rode `chcp 65001` antes ou use:

```bash
java -Dstdout.encoding=UTF-8 App
```

## Decisões técnicas

- **Buffer:** `ArrayList<String>`, onde cada elemento é uma linha. A lista cresce e encolhe conforme as linhas são inseridas e removidas.
- **Arquivos:** `java.nio.file` (`Path` e `Files`). A leitura e a escrita usam UTF-8.
- **Exceções tratadas:** `NumberFormatException` e `IndexOutOfBoundsException` (número de linha inválido), e `NoSuchFileException`, `AccessDeniedException` e `IOException` (gravação em disco). As duas primeiras de gravação são subclasses de `IOException`, por isso vêm antes dela nos `catch`.
- **Falha na leitura:** se o arquivo existe mas não pode ser lido, o programa encerra em vez de seguir com buffer vazio, para que um `:w` posterior não apague o conteúdo original.

## Testes manuais realizados

- `:w` em caminho válido, seguido de `:q` sem pergunta de confirmação.
- Inserir texto, `:q` e responder `n`: a saída é cancelada e a edição continua.
- Salvar texto com acentos, reabrir o arquivo e conferir o carregamento.
- `:w` em pasta inexistente: o erro é mostrado e o editor continua aberto.

## Limitações conhecidas

- Arquivos salvos em ANSI por outros programas podem falhar na leitura, porque o programa espera UTF-8.
- Se o nome informado for de uma pasta, e não de um arquivo, o programa encerra com mensagem de erro.
- Não há desfazer (`undo`), busca nem navegação por cursor como no VI real.
- Não há testes automatizados.