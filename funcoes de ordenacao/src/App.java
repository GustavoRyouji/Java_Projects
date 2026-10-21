import java.util.Scanner;
import java.util.Arrays;

public class App {
    static int[] vetor = {150, 60, 896, 654, 12, 3, 78, -10, -150};
    static int [][] matriz = { {25, 60, 48},
    {87, -10, 25},
    {-15, 856, 45},
    {85, -47, 15},
    {-9, 54, 16}};
    static Scanner sc = new Scanner(System.in);
    static int colunaSelecionada = 0;

    public static void main(String[] args) throws Exception {

        System.out.println("=====Ordenador de Vetores=====");
        int[] vetorOrdenado = ordenacaoVetor(vetor);
        System.out.println("Vetor Ordenado:\n " + Arrays.toString(vetorOrdenado));

        System.out.println("=====Ordenador de Matrizes=====");
        int[][] matrizOrdenada = ordenacaoMatriz(matriz);
        System.out.println("Matriz Ordenada:\n ");
        imprimirMatriz(matrizOrdenada);

    }

    //funções

    static int[] ordenacaoVetor(int[] vetor){
        String opcao;
        System.out.print("A ordem deve ser Crescente ou Decrescente?: ");
        opcao = sc.nextLine().trim().toUpperCase();

        boolean crescente = true;
        
        if (opcao.equals("CRESCENTE")){
            for (int i = 0; i < vetor.length - 1; i++){
                for (int j = 0; j < vetor.length - 1 - i; j++){
                    if ((crescente && vetor[j] > vetor[j+1]) || (!crescente && vetor[j] < vetor[j + 1])){
                        int aux = vetor[j];
                        vetor[j] = vetor[j + 1];
                        vetor[j + 1] = aux;
                    }
                }
            }
        }
        else {
            crescente = false;
            for (int i = 0; i < vetor.length - 1; i++){
                for (int j = 0; j < vetor.length - 1 - i; j++){
                    if ((crescente && vetor[j] > vetor[j+1]) || (!crescente && vetor[j] < vetor[j + 1])){
                        int aux = vetor[j];
                        vetor[j] = vetor[j + 1];
                        vetor[j + 1] = aux;
                    }
                }
            }
        }
        return vetor;
    }


    static int[][] ordenacaoMatriz(int[][]matriz){
        int coluna = 0;
        boolean crescente;

        System.out.print("Digite o número da coluna deve ser ordenada [0 a 2]: ");
        coluna = sc.nextInt();
        sc.nextLine();

        colunaSelecionada = coluna;

        System.out.println("A ordem deve ser Crescente ou Decrescente?: ");
        String opcao = sc.nextLine().trim().toUpperCase();

        if (opcao.equals("CRESCENTE")){
            crescente = true;
            for (int i = 0; i < matriz.length - 1; i++){
                for (int j = 0; j < matriz.length - 1 - i; j++){
                    if ((crescente && matriz[j][coluna] > matriz[j + 1][coluna]) || (!crescente && matriz[j][coluna] < matriz[j + 1][coluna])){
                        int[] aux = matriz[j];
                        matriz[j] = matriz[j + 1];
                        matriz[j + 1] = aux;
                    }
                }
            }
        }else{
            crescente = false;
            for (int i = 0; i < matriz.length - 1; i++){
                for (int j = 0; j < matriz.length - 1 - i; j++){
                    if ((crescente && matriz[j][coluna] > matriz[j + 1][coluna]) || (!crescente && matriz[j][coluna] < matriz[j + 1][coluna])){
                        int[] aux = matriz[j];
                        matriz[j] = matriz[j + 1];
                        matriz[j + 1] = aux;
                    }
                }
            }
        }
        return matriz;

    }

    static void imprimirMatriz(int[][] matrizOrdenada){
        for (int i = 0; i < matrizOrdenada.length; i++){
            for (int j = 0; j < matrizOrdenada[i].length; j++){
                if (j == colunaSelecionada) {
                    System.out.printf(" [%3d] \t", matrizOrdenada[i][j]);

                } else {
                    System.out.printf("  %3d  ", matrizOrdenada[i][j]);
                }
                
            }
            System.out.println();
        }
    }

    static void matrizsimples(int[][] matrizOrdenada){
        for (int i = 0; i < matrizOrdenada.length; i++){
            for (int j = 0; j < matrizOrdenada[i].length; j++){
                System.out.print(matrizOrdenada[i][j] + " ");
            }
            System.out.println();
        }
    }
}

