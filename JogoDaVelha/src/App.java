import java.util.Scanner;

public class App {
    //criação das variáveis.
    static char[] letras = {'A', 'B', 'C'};
    static boolean vez = true;
    static char[][] tabuleiro = new char[3][3];


    public static void main(String[] args) throws Exception {
        
        //apresentação do jogo.
        System.out.println("\n=====Jogo da Velha=====");
        iniciarTabuleiro(tabuleiro);
        imprimirTabuleiro(tabuleiro);
        String entrada = "";
        
        
        //escolha do jogador.
        Scanner sc = new Scanner(System.in);
        
        while(verificarVitoria(tabuleiro) == false && verificarEmpate(tabuleiro) == false){
        
            if (vez == true){
                System.out.print("Jogador 1, escolha uma posição: ");
            entrada = sc.nextLine();
            }else{
            System.out.print("Jogador 2, escolha uma posição: ");
            entrada = sc.nextLine();
        }
        //verifica se tem só dois caracteres.
        if (entrada.length() != 2) {
        System.out.println("Posição inválida!");
        continue;}

        
        //adiciona os valores no tabuleiro.
        char coluna = entrada.charAt(0);
        char linha = entrada.charAt(1);

        //verifica se o valor é válido.
        if (coluna < '1' || coluna > '3' ||
        linha < 'A' || linha > 'C') {
        System.out.println("Posição inválida!");
        continue;}

        boolean jogavaValida = adicionarPosicao(tabuleiro, coluna, linha);
        
        if(jogavaValida == true)
        vez = !vez;
    

        //resultado
        imprimirTabuleiro(tabuleiro);
        
    }
     if (verificarVitoria(tabuleiro)) {

    if (vez == false) {
        System.out.println("_____Jogador 1 venceu!_____");
    } else {
        System.out.println("_____Jogador 2 venceu!_____");
    }

} else {
    System.out.println("_____Jogo Empatado!_____");
    
}
        sc.close();
    }

    //funções

    static void iniciarTabuleiro(char[][]tabuleiro){
    for(int i = 0; i < 3; i++){
        for(int c = 0; c < 3; c++){
           tabuleiro[i][c] = '-';
            }
        }
    }
    static void imprimirTabuleiro(char[][]tabuleiro){
        System.out.println("    1   2   3");
    for(int i = 0; i < 3; i++){
        System.out.print(letras[i] + "   ");

        for(int c = 0; c < 3; c++){
           System.out.print(tabuleiro[i][c] + "   ");
            }
        System.out.println();
        }
    }

    static boolean adicionarPosicao(char[][] tabuleiro, char coluna, char linha) {

    int indiceLinha = linha - 'A';
    int indiceColuna = coluna - '1';

    // verifica se a posição já está ocupada
    if (tabuleiro[indiceLinha][indiceColuna] != '-') {
        System.out.println("Essa posição já está ocupada!");
        return false;
    }

    // coloca a peça do jogador
    if (vez == true) {
        tabuleiro[indiceLinha][indiceColuna] = 'X';
    } else {
        tabuleiro[indiceLinha][indiceColuna] = 'O';
    }return true;
}

    static boolean verificarVitoria(char[][]tabuleiro){

        for (int i = 0; i <3; i++){

            if (tabuleiro[i][0] != '-' &&
            tabuleiro[i][0] == tabuleiro[i][1] &&
            tabuleiro[i][1] == tabuleiro[i][2]){
                return true;

            }else if (tabuleiro[0][i] != '-' && 
            tabuleiro[0][i] == tabuleiro[1][i] && 
            tabuleiro[1][i] == tabuleiro[2][i]){
                return  true;
            }
            }
            if (tabuleiro[0][0] != '-' &&
            tabuleiro[0][0] == tabuleiro[1][1] &&
            tabuleiro[1][1] == tabuleiro[2][2]) {
                return true;}

            if (tabuleiro[0][2] != '-' &&
                tabuleiro[0][2] == tabuleiro[1][1] &&
                tabuleiro[1][1] == tabuleiro[2][0]) {
                return true;}
                
            return false;
    }

    static boolean verificarEmpate(char[][] tabuleiro) {
    for (int i = 0; i < 3; i++) {
        for (int c = 0; c < 3; c++) {

            if (tabuleiro[i][c] == '-') {
                return false;
            }

        }
    }

    return true;
}

}
