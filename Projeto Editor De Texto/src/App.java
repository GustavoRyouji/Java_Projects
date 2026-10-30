import java.util.Scanner;
import java.util.ArrayList; // Importação necessária para o ArrayList
import java.util.List; // Importação necessária para a List
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.nio.file.AccessDeniedException;

public class App {
    static Path path;
    static Scanner sc = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
    static String modo = "COMANDO";
    static boolean modificado = false;

    //BUffer: uma lista que representa as linhas na memória.
    static ArrayList <String> buffer = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        path = lerArquivo();
        //inicializa o buffer com o conteúdo do disco (ou vazio)
        buffer = inicializarBuffer(path);

        boolean continuarEditor = true;
        
        while (continuarEditor) {
            exibir(path, buffer, modo);

            String comando = sc.nextLine();
            if (modo.equals("COMANDO")){
                continuarEditor = modoDeComando(comando.trim());
            }else if (modo.equals("INSERÇÃO")){
                modoDeInsercao(comando);
            }else{
                continuarEditor = modoDeLinhaDeComando(comando.trim());
            }
        }
        System.out.println("Processo do Editor Java-VI encerrado com sucesso.");
    } //main


    //funções

    static Path lerArquivo(){
        String entradaUsuario;
        Path caminhoGerado = null;

        do{
        System.out.println("Digite o nome do Arquivo: ");
        entradaUsuario = sc.nextLine().trim();
        
        try{
        if (entradaUsuario.isEmpty()){
            System.out.println("Erro. Nenhum valor digitado. Digite um valor válido: ");
            continue;
        }

        caminhoGerado = Paths.get(entradaUsuario);

        if (Files.exists(caminhoGerado)) {
            System.out.println("O arquivo existe! O programa pode abri-lo para edição.");
        } else {
            System.out.println("O arquivo NÃO existe! O programa criará um novo arquivo ao salvar.");
        }
        break;
        }catch(InvalidPathException e){
            System.out.println("Erro. O nome do arquivo contém caracteres inválidos.");
            }
        }while(true);
            return caminhoGerado;
    }
    

    static ArrayList<String> inicializarBuffer(Path caminhoDoArquivo){
        //se o arquivo não existe -> devolve uma lista vazia:
        if (!Files.exists(caminhoDoArquivo)){
            System.out.println("[Buffer]  Arquivo novo detectado. Iniciando buffer vazio.\n");
            return new ArrayList<>();
        }

        //senão -> lê todas as linhas do arquivo e devolve como ArrayList<String>
        try{
            System.out.println("[Buffer] Carregando linhas do disco para a memória...");

            // Files.readAllLines retorna uma List<String>. 
            // Nós a envolvemos em um 'new ArrayList<>' para garantir que seja uma lista mutável (editável).
            List<String> linhasDoArquivo = Files.readAllLines(caminhoDoArquivo);
            return new ArrayList<>(linhasDoArquivo);
        }catch (IOException e){
            System.out.println("\n[ERRO CRÍTICO] Falha de leitura: Não foi possível carregar o arquivo existente!");
            System.out.println("Motivo detalhado: " + e.getMessage());
            System.out.println("Para proteger seus dados e evitar que o arquivo seja apagado, o programa será encerrado.");
        
            // Encerra a execução do Java imediatamente com código de erro (1)
            System.exit(1); 
        
            // O Java exige um retorno aqui para compilar, embora o System.exit impeça de chegar nesta linha
            return null;
        }
    }


    static void exibir(Path caminhoDoArquivo, ArrayList <String> buffer, String modoAtual){
        System.out.flush(); //força o terminal a processar tudo o que está na fila da memória na mesma hora.

        //Cabeçalho do Editor
        System.out.println("========================================================================");
        System.out.println("                         EDITOR DE TEXTO JAVA-VI                        ");
        System.out.println("========================================================================");

         // Determina o rótulo do arquivo (Adiciona [Novo Arquivo] se não existir no disco)
         String rotuloArquivo = caminhoDoArquivo.getFileName().toString();
         if (!Files.exists(caminhoDoArquivo)){
            rotuloArquivo += " [Novo Arquivo] ";
         }
         System.out.println(" Arquivo atual: " + rotuloArquivo);
        System.out.println("------------------------------------------------------------------------");

        //Área do Buffer (conteúdo)
        if (buffer.isEmpty()){
            // Exibe as tradicionais linhas vazias do VI (~) com o aviso centralizado
            System.out.println("~");
            System.out.println("~                     [ Buffer está vazio ]");
            System.out.println("~             Digite comandos ou insira texto para começar.");
            System.out.println("~");

        }else{
            //exibe o buffer com as linnhas numeradas sequencialmente
            for (int i = 0; i < buffer.size(); i++){
                // Formata o número da linha para manter o alinhamento visual (ex: "  1 | ")
                System.out.printf("%3d | %s%n", (i + 1), buffer.get(i));
            }
        }
         // Preenche algumas linhas vazias estilizadas até o final da tela (opcional, como no VI)
         int linhasRestantes = 8 - buffer.size();
         for (int i = 0; i < linhasRestantes; i++){
            if (buffer.isEmpty() && i < 4) continue;
            System.out.println("~");
         }

         // 4. Barra de Status com o modo atual (Rodapé)
         System.out.println("------------------------------------------------------------------------");
        System.out.printf("                                                    Total: %d linha(s)%n", 
                        buffer.size());
        if (modoAtual.equals("LINHA DE COMANDO")) {
            System.out.println("[:w] Salvar Arquivo");
            System.out.println("[:q] Sair do Editor");
            System.out.println("[:wq] ou [:x] Salvar e Sair");
            System.out.println("[:q!] Sair Sem Salvar");
        } else if (modoAtual.equals("INSERÇÃO")) {
            System.out.println("Digite o texto da nova linha e aperte ENTER.");
            System.out.println("Digite 'ESC' ou ':ESC' para voltar ao Modo de Comando.");
        }else {
            System.out.println("[i] Inserir Linha");
            System.out.println("[d] Excluir Linha");
            System.out.println("[e] Editar Linha");
            System.out.println("[:] Modo Linha de Comando");
        }

        System.out.printf("[MODO: %s]%n", modoAtual);
        System.out.println("========================================================================");
        System.out.print("Digite o comando: ");
            }


    static boolean modoDeComando(String comando){
        
            
            // --- TRATAMENTO DOS COMANDOS 'e' E 'd' (NumberFormatException e IndexOutOfBoundsException) ---
            //TRATAMENTO 'd'
            if (comando.equals("d") || comando.startsWith("d ")) {
                String texto = comando.substring(1).trim();     // o que vier depois do "d"

                if (texto.isEmpty()){    // veio só "d": pergunta
                    System.out.print("Número da linha a excluir: ");
                    texto = sc.nextLine().trim();
                }

                int indice = converterLinha(texto);
                if (indice != -1){
                    buffer.remove(indice);
                    modificado = true;
                    System.out.println("Linha " + (indice + 1) + " deletada do buffer.");
                }
            }

            //TRATAMENTO 'e'
            else if (comando.equals("e") || comando.startsWith("e ")) {
                String texto = comando.substring(1).trim();

                if (texto.isEmpty()){
                    System.out.print("Número da linha a editar: ");
                    texto = sc.nextLine().trim();
                }

                int indice = converterLinha(texto);
                if (indice != -1){
                    System.out.println("Texto Atual: " + buffer.get(indice));
                    System.out.println("Digite o novo texto desejado: ");
                    String novoTexto = sc.nextLine();
                    buffer.set(indice, novoTexto);
                    modificado = true;
                }
            }
            //comando de inserção
            else if (comando.equals("i")){
                modo = "INSERÇÃO";
                System.out.println("Entrando no Modo de Inserção. Digite seu texto livremente.");
                System.out.println("Para voltar ao Modo Comando, digite 'ESC' ou ':ESC' e aperte ENTER.");
                return true;
            }
            else if (comando.startsWith(":")) {       // ← o ramo ÚNICO, NOVO
                if (comando.equals(":")) {
                    modo = "LINHA DE COMANDO";        // só os dois pontos: muda o modo
                } else {
                    return modoDeLinhaDeComando(comando);   // ":w" direto: chama o método na hora
                }
                }else {
                    System.out.println("Comando não reconhecido.");
                }

            return true;
    }

    static void modoDeInsercao(String linha) {
        if (linha.trim().equalsIgnoreCase("ESC") || linha.trim().equalsIgnoreCase(":ESC")){
            modo = "COMANDO";
            System.out.println("Voltando para o Modo de Comando...");
        }else{
            buffer.add(linha);
            modificado = true;
        }
}


    static int converterLinha(String texto){
        try{
            int linhaHumana = Integer.parseInt(texto);
            int indice = linhaHumana - 1;

            if (indice < 0 || indice >= buffer.size()) {
                throw new IndexOutOfBoundsException("A linha " + linhaHumana + " não existe no buffer (Tamanho atual: " + buffer.size() + ").");
            }
            return indice;
        }catch (NumberFormatException e) {
            System.out.println("Erro [NumberFormatException]: '" + texto + "' não é um número de linha válido.");
                    
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erro [IndexOutOfBoundsException]: " + e.getMessage());
        }return -1; //valor sntinela para dizer que algo deu errado. 
    }


    static boolean modoDeLinhaDeComando (String entrada){
        modo = "COMANDO";

        String cmd = entrada.trim();
        if (cmd.startsWith(":")){
            cmd = cmd.substring(1);
        }

        if (cmd.equals("w") || cmd.equals("wq") || cmd.equals("x")){
            // TRATAMENTO DOS COMANDOS :w E :wq (IOException)
                try {
                    System.out.println("[Disco] Gravando alterações...");
                    Files.write(path, buffer); // Pode gerar IOException se a pasta não existir ou faltar permissão
                    modificado = false;
                    System.out.println("Arquivo " + path.getFileName() + " salvo com sucesso! (" + buffer.size() + " linha(s))");
                    
                    
                    
                    if (cmd.equals("wq") || cmd.equals("x")) {
                        System.out.println("Saindo do editor. Até logo!");
                        return false; // Encerra o programa
                    }   
                } catch (NoSuchFileException e) {
                    System.out.println("Erro [:w - NoSuchFileException]: a pasta do arquivo não existe: " + e.getMessage());
                } catch (AccessDeniedException e) {
                    System.out.println("Erro [:w - AccessDeniedException]: sem permissão para gravar em: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Erro [:w - IOException]: Falha ao gravar no disco. Motivo: " + e.getMessage());
                }
            }
            // Comando genérico de saída sem salvar
            else if (cmd.equals("q")) {
                if (modificado){
                    System.out.println("Há alterações não salvas. Os dados serão perdidos se sair agora.");
                    System.out.print("Deseja continuar e sair? (s/n): ");
                    String resposta = sc.nextLine().trim();

                    if (resposta.equalsIgnoreCase("s")){
                        System.out.println("Saindo sem salvar...");
                        return false;
                    }
                    System.out.println("Saída cancelada...");
                }else{
                    System.out.println("Saindo...");
                    return false;
                }
            }else if (cmd.equals("q!")){
                System.out.println("Saindo sem salvar...");
                return false;
            }else{
                System.out.println("Comando de linha não reconhecido.");
            }
            
            return true;
        }

} //classe App
