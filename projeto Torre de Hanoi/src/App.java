import java.util.Scanner;

public class App {
    //desenvolvido por Gustavo Ryouji Amano Ferreira

    static final int PINOS = 3; //criar uma constante de classe — ou seja, um valor fixo que pertence à própria classe (e não a uma instância/objeto específico) e que 
    // nunca pode ser alterado depois de definido.
    static  final int MAX_DISCOS = 7;

    static int[][] pinos = new int[PINOS][MAX_DISCOS]; // 0 = vazio.
    static int[] qtd = new int[PINOS];                 //discos em cada pino.
    static int numDiscos;
    static int movimentos = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        numDiscos = lerQuantidadeDiscos();
        inicializar();

        boolean jogando = true;
        
        //começo do jogo:

        while(jogando){
            exibir();

            //captura o array de jogada retornada pela função:
            int[] jogada = lerPino();
            int origem = jogada[0];
            int destino = jogada[1];

            String erro = ValidarMove(pinos, qtd, origem, destino);
            if (!erro.equals("")){
                //se o erro não for vazio, imprime o erro e ignora o resto do laço:
                System.out.println(erro);
                continue;
            }

            //Se passou da validação, o movimento é válido:
            System.out.println("Movimento Válido. Atuializando...");
            moverDisco(origem, destino);

            //aumenta o contador de movimentos:
            movimentos++;
            System.out.println("Movimentos realizados até agora: " + movimentos);

            //verifica vitória:
            if (qtd[2] == numDiscos){
                jogando = false;
            }

        }
            exibir();
            System.out.println("Parabéns, você venceu! \n movimentos realizados: " + movimentos);


    }



    //funções

    static int lerQuantidadeDiscos(){
        String entrada;
        int discos = 0;
        boolean valor_valido = false; //controla se o laço deve continuar
        do{
            System.out.println("Digite a quantidade de discos para a Torre de Hanói (entre 3 e 7): ");
            entrada = sc.nextLine(); // 1. Lê a linha como String);

        try {
            //tenta converter para inteiro
            discos = Integer.parseInt(entrada);

            if (discos < 3 || discos > 7){
                System.out.println("Erro: O número está fora do intervalo! Deve ser entre 3 e 7.");
            }else{
                valor_valido = true; // Número é válido e está no intervalo, pode sair do laço
            }
        }catch (NumberFormatException e){
            //captura o erro caso o usuário tenha colocado letras ou símbolos
            System.out.println("Erro: Você digitou um número inválido!");
        }

    }while (!valor_valido); // O laço só termina quando valorValido for true
        return discos; //retorna o valor final de discos
    }



    static void inicializar(){
        // 1. Laço de 0 até numDiscos - 1 para preencher a primeira torre (índice 0)
        for (int i = 0; i < numDiscos; i++){
            // Quando i=0, coloca 4 (base). Quando i=3, coloca 1 (topo).
            pinos[0][i] = numDiscos - i;
        }
        // 2. Ajusta a quantidade de discos na primeira torre
        qtd[0] = numDiscos;

    }



    static void exibir(){
        System.out.println("\n--- TORRES DE HANÓI ---");

        //Laço externo: percorre as linhas do topo (MAX_DISCOS - 1) até a base (0)
        for (int linha = MAX_DISCOS - 1; linha>=0; linha--){
            //Laço interno: percorre os 3 pinos (0, 1 e 2)
            for (int pino = 0; pino < 3; pino++){
                int tamanhoDisco = pinos[pino][linha]; //pega o disco naquela posição

                if (tamanhoDisco == 0){
                    // Se estiver vazio (0), imprime o pino centralizado
                System.out.print("      |       ");
                }else{
                    // Cria o desenho do disco (ex: "###")
                String desenho = "#".repeat(2 * tamanhoDisco - 1);
                // Cria as abas do disco para centralizar
                // Isso garante que o meio do disco se alinhe sempre com o pino "|"
                String espaco = " ".repeat(MAX_DISCOS - tamanhoDisco);
                System.out.print(espaco + desenho + espaco + " ");
                }
            }
            System.out.println(); //quebra a linha para ir para o próximo nível de cima para baixo
        }
         // Imprime a base das torres para dar acabamento visual
        System.out.println("============= ============= =============");
        System.out.println("    PINO 1        PINO 2        PINO 3    \n");
    }



    static int[] lerPino(){
        String entrada;
        int[] jogada = new int[2];
        boolean jogada_valida = false; //controla se o laço deve continuar.

        do{
            System.out.println("Digite o pino de origem e o pino de destino (ex. 1 3):" );
            entrada = sc.nextLine().trim(); //trim pra ele remover os espaços das pontas.
            
            String[] partes = entrada.split("\\s+"); //garante q sejam apenas 2 números.
            
            if (partes.length != 2){
                System.out.println("Erro. Digite exatamente 2 números inteiros.");
                continue; //volta para o início do Do-While.
            }

            try{
                int origem = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);

                if (origem < 1 || origem > 3 || destino < 1 || destino > 3){
                    System.out.println("Erro. Escolha um pino entre 1 e 3.");
                } else{
                    jogada[0] = origem - 1;
                    jogada[1] = destino - 1;
                    jogada_valida = true;
                }

            } catch (NumberFormatException e){
                System.out.println("Erro: Entrada inválida! Certifique-se de usar apenas números.");
            }

        } while (!jogada_valida);
            return jogada;
    }



    static String ValidarMove (int[][] pinos, int[] qtd, int origem, int destino){
        //checagem: origem igual destino.
        if (origem == destino) {
            return "Erro. A origem e o destino não podem ser iguais";
        }

        //checagem: não há disco na origem.
        if (qtd[origem] == 0){
            return "Erro. O pino de origem não possui discos.";
        }
        
        //se passou, a jogada é válida.
        int TopoOrigem = pinos[origem][qtd[origem] - 1];

        if (qtd[destino] > 0){
            //Checagem: Destino tem disco? Se sim, valida o tamanho
            int TopoDestino = pinos[destino][qtd[destino] - 1];
            
            // O disco da origem não pode ser maior que o do destino
            if (TopoOrigem > TopoDestino){
                return "Erro. Movimento inválido. Você não pode colocar um disco maior em cima de um menor.";
            }
            }
             return "";
    }



    static void moverDisco(int origem, int destino){
        //passo 1: guardar o disco do topo da origem
        //o topo atual está no índice pinos[origem][qtd[origem] - 1]:
        int discoMover = pinos[origem][qtd[origem] - 1];

        //passo 2: zerar a posição na matriz (qtd[origem] - 1):
        pinos[origem][qtd[origem] - 1] = 0;

        //passo 3: colocar o disco na proóxima posição livre no destino:
        //o próximo lugar livre é exatamente o índice atual de qtd[destino]
        pinos[destino][qtd[destino]] = discoMover;

        //passo 4: limpar o qtd dos dois pinos:
        qtd[origem]--;
        qtd[destino]++;
    }

}
