import java.util.Scanner;

public class App {
    static char[][] matriz = new char[14][9];
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String palavra = "JAVA";
        char[] palavraOculta = new char[palavra.length()];
        for (int i = 0; i < palavraOculta.length; i++){
            palavraOculta[i] = '_';
        }
        int tentativas = 6;
        boolean venceu = false;

        System.out.println("====JOGO DA FORCA====");

        while (tentativas > 0 && !venceu){
            //mostra a palavra
            desenharForca(matriz);
            desenharBoneco(matriz, tentativas);
            ImprimirTabuleiro(matriz);
            System.out.print("\npalavra: ");
            for (char c : palavraOculta){
                System.out.print(c + " ");
            }
            System.out.println("\n Tentativas Restantes: " + tentativas);

            System.out.print("Digite uma Letra: ");
            char letra = sc.next().toUpperCase().charAt(0);

            boolean acertou = false;

            //procura a letra na palavra
            for (int i = 0; i < palavra.length(); i++){
                if(palavra.charAt(i) == letra){
                    palavraOculta[i] = letra;
                    acertou = true;
                }
            }
            if (acertou){
                System.out.println("Acertou");
            }else{
                System.out.println("Errou");
                tentativas --;
            }
             // Verifica se ainda existe "_"
            venceu = true;

            for (char c : palavraOculta) {
                if (c == '_') {
                    venceu = false;
                    break;
                }
            }
        }

            if (venceu) {
                System.out.println("\nParabéns! Você descobriu a palavra: " + palavra);
            } else {
                System.out.println("\nVocê perdeu!");
                System.out.println("A palavra era: " + palavra);
            }

            sc.close();
        }   

    //funções

    static void desenharForca(char[][] matriz) {
    // Limpa a matriz
    for (int i = 0; i < matriz.length; i++) {
        for (int c = 0; c < matriz[i].length; c++) {
            matriz[i][c] = ' ';
        }
    }
    // Estrutura da forca
    for (int i = 1; i < 12; i++) {
        matriz[i][1] = '|';
    }

    for (int c = 1; c < 7; c++) {
        matriz[1][c] = '-';
    }

    matriz[2][6] = '|';
    matriz[3][6] = '|';

    for (int c = 0; c < 5; c++) {
        matriz[12][c] = '-';
    }
}
    static void desenharBoneco(char[][] matriz, int tentativas) {

    if (tentativas <= 5) {
        matriz[4][6] = 'O';
    }

    if (tentativas <= 4) {
        matriz[5][6] = '|';
    }

    if (tentativas <= 3) {
        matriz[5][5] = '/';
    }

    if (tentativas <= 2) {
        matriz[5][7] = '\\';
    }

    if (tentativas <= 1) {
        matriz[6][5] = '/';
    }

    if (tentativas <= 0) {
        matriz[6][7] = '\\';
    }
}


    static void ImprimirTabuleiro(char matriz[][]){
        for (int i = 0; i < matriz.length; i++){
            for (int c = 0; c < matriz[i].length; c++){
                System.out.print(matriz[i][c] + " ");
            }
            System.out.println();
        }
    }
    
}
