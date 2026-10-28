import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    static double[][] matriz = new double[100][4]; //enunciado 2
    static int totalRegistros = 0;
    static Scanner scanner = new Scanner(System.in);
    static final String NOME_ARQUIVO = "abastecimentos.txt";

    public static void main(String[] args) throws Exception {
        
        // enunciados 8 e 11
        carregarDados();

        int opcao = 0;

        // enunciado 9
        do {
            System.out.println("\n=== MENU DE OPÇÕES ===");
            System.out.println("1 - Listar Abastecimentos");
            System.out.println("2 - Inserir Novo Abastecimento");
            System.out.println("3 - Exibir Média de consumo");
            System.out.println("4 - Finalizar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();  //enunciado 10
                switch (opcao) {
                    case 1:
                        listarAbastecimentos();
                        break;
                    case 2:
                        inserirAbastecimento(scanner);
                        break;
                    case 3:
                        exibirMediaConsumo();
                        break;
                    case 4:
                        // enunciado 12
                        gravarDados();
                        System.out.println("Programa finalizado com sucesso e dados salvos!");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, digite um número inteiro válido.");
                scanner.nextLine(); // Limpa o buffer do scanner
                opcao = 0;
            }
        } while (opcao != 4);

        scanner.close();
    }

    //funções

    static void primeiroValor(int... valores) {  //enunciado 3
        if (totalRegistros >= 100) {
            System.out.println("Erro: A matriz está cheia (limite de 100 registros atingido).");
            return;
        }

        double km = 0, litros = 0, valorLitro = 0;
        boolean dadosValidos = false;

        while (!dadosValidos){
            try{
                System.out.print("Digite o Km atual (ex: 40567): ");
                km = scanner.nextDouble();
                if (totalRegistros > 0 && km <= matriz[totalRegistros - 1][0]) {
                    System.out.println("Aviso: O Km digitado é menor ou igual ao último registrado.");
                }
                System.out.print("Digite a quantidade de litros abastecidos (ex: 50): ");
                litros = scanner.nextDouble();

                System.out.print("Digite o valor do litro de combustível(ex: 5,49): ");
                valorLitro = scanner.nextDouble();

                if (km < 0 || litros < 0 || valorLitro < 0){
                    System.out.println("Erro: Os valores inseridos devem ser maiores que zero.");
                    continue;
                }
                dadosValidos = true;
            } catch (InputMismatchException e) {
                System.out.println("Erro de digitação: Certifique-se de usar a vírgula para decimais se seu sistema estiver em português.");
                scanner.nextLine(); // Limpa o buffer
                break; // Sai do loop para evitar loop infinito em caso de erro de digitação
            }

            matriz[totalRegistros][0] = km;
            matriz[totalRegistros][1] = litros;
            matriz[totalRegistros][2] = valorLitro;
            totalRegistros++;

            calcularValorGasto(totalRegistros);

            System.out.println("Abastecimento registrado com sucesso na linha " + (totalRegistros + 1) + "!");
            totalRegistros++;
        }
    }

    static void listarAbastecimentos(){ // enunciado 4
        if (totalRegistros == 0) {
            System.out.println("Nenhum dado foi registrado.");
            return;
        }
        System.out.println();
        System.out.println("========================Lista de Abastecimentos: ========================");
        System.out.printf("%-5s | %-12s | %-12s | %-15s | %-12s\n", "Reg", "Km Atual", "Litros", "Valor/Litro (R$)", "Total Gasto (R$)");
        for (int i = 0; i < totalRegistros; i++) {
            System.out.printf("%-5d | %-12.1f | %-12.2f | %-15.2f  | %-12.2f\n",
                    (i + 1), matriz[i][0], matriz[i][1], matriz[i][2], matriz[i][3]);
        }
        System.out.println("=========================================================================");
    }


    static void calcularValorGasto(int linha) { //enunciado 5
        matriz[linha][3] = matriz[linha][1] * matriz[linha][2];
    }

    static void exibirMediaConsumo(){ //enunciado 6
        if (totalRegistros < 2) {
            System.out.println("São necessários pelo menos 2 registros para calcular a média de consumo.");
            return;
        }

        double primeiroKm = matriz[0][0];
        double ultimoKm = matriz[totalRegistros - 1][0];
        double diferencaKm = ultimoKm - primeiroKm;

        double somatoniaLitros = 0;

        for (int i = 1; i < totalRegistros; i++) {
            somatoniaLitros += matriz[i][1];
        }

        if (somatoniaLitros == 0) {
            System.out.println("Erro: A soma dos litros abastecidos (desconsiderando a primeira linha) é zero.");
            return;
        }

        double media = diferencaKm / somatoniaLitros;
        System.out.printf("\n--- MÉDIA DE CONSUMO GERAL ---\n");
        System.out.printf("Km rodados totais: %.1f km\n", diferencaKm);
        System.out.printf("Litros considerados: %.2f L\n", somatoniaLitros);
        System.out.printf("Média de consumo: %.2f km/L\n", media);
    }

    static void gravarDados() {   //enunciado 7
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (int i = 0; i < totalRegistros; i++) {
                writer.write(matriz[i][0] + ";" + matriz[i][1] + ";" + matriz[i][2] + ";" + matriz[i][3]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados no arquivo: " + e.getMessage());
        }
    }

    static void carregarDados() { //enunciado 8
        try (Scanner fileScanner = new Scanner(new java.io.File(NOME_ARQUIVO))) {
            while (fileScanner.hasNextLine() && totalRegistros < 100) {
                String[] linha = fileScanner.nextLine().split(";");
                if (linha.length == 4) {
                    matriz[totalRegistros][0] = Double.parseDouble(linha[0]);
                    matriz[totalRegistros][1] = Double.parseDouble(linha[1]);
                    matriz[totalRegistros][2] = Double.parseDouble(linha[2]);
                    matriz[totalRegistros][3] = Double.parseDouble(linha[3]);
                    totalRegistros++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar os dados do arquivo: " + e.getMessage());
        }
    }

    static void inserirAbastecimento(Scanner scanner) { //enunciado 9
        if (totalRegistros >= 100) {
            System.out.println("Erro: A matriz está cheia (limite de 100 registros atingido).");
            return;
        }

        double km = 0, litros = 0, valorLitro = 0;
        boolean dadosValidos = false;

        while (!dadosValidos) {
            try {
                System.out.print("Digite o Km atual (ex: 40567): ");
                km = scanner.nextDouble();
                if (totalRegistros > 0 && km <= matriz[totalRegistros - 1][0]) {
                    System.out.println("Aviso: O Km digitado é menor ou igual ao último registrado.");
                }

                System.out.print("Digite a quantidade de litros abastecidos (ex: 36,78): ");
                litros = scanner.nextDouble();

                System.out.print("Digite o valor do litro de combustível (ex: 3,68): ");
                valorLitro = scanner.nextDouble();

                if (km <= 0 || litros <= 0 || valorLitro <= 0) {
                    System.out.println("Erro: Os valores inseridos devem ser maiores que zero.");
                    continue;
                }

                dadosValidos = true;
            } catch (InputMismatchException e) {
                System.out.println("Erro de digitação: Certifique-se de usar a vírgula para decimais se seu sistema estiver em português.");
                scanner.nextLine(); // Limpa o buffer
            }
        }

        // Armazena os valores na linha atual
        matriz[totalRegistros][0] = km;
        matriz[totalRegistros][1] = litros;
        matriz[totalRegistros][2] = valorLitro;

        // enunciado 5
        calcularValorGasto(totalRegistros);

        System.out.println("Abastecimento registrado com sucesso na linha " + (totalRegistros + 1) + "!");
        totalRegistros++;
    }

}//App.java
