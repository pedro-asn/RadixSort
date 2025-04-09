import java.io.*;
import java.util.*;

public class GeradorVetores {
    private static final int[] TAMANHOS = {100, 1000, 10000, 100000, 500000};
    private static final int[] PERCENTUAIS = {10, 30, 50};

    public static void main(String[] args) {
        for (int tamanho : TAMANHOS) {
            // Gerar vetores parcialmente desordenados (crescente)
            for (int percentual : PERCENTUAIS) {
                gerarVetorParcialmenteCrescente(tamanho, percentual);
            }

            // Gerar vetores parcialmente desordenados (decrescente)
            for (int percentual : PERCENTUAIS) {
                gerarVetorParcialmenteDecrescente(tamanho, percentual);
            }

            // Gerar vetor totalmente desordenado
            gerarVetorTotalmenteDesordenado(tamanho);
        }
    }

    // Gera vetor parcialmente desordenado em ordem crescente
    private static void gerarVetorParcialmenteCrescente(int tamanho, int percentualDesordem) {
        int[] vetor = new int[tamanho];

        // Preencher o vetor em ordem crescente
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = i;
        }

        // Calcular quantos elementos serão desordenados
        int qtdDesordem = (int) (tamanho * (percentualDesordem / 100.0));
        Random random = new Random();

        // Desordenar a quantidade especificada de elementos
        for (int i = 0; i < qtdDesordem; i++) {
            int pos1 = random.nextInt(tamanho);
            int pos2 = random.nextInt(tamanho);
            int temp = vetor[pos1];
            vetor[pos1] = vetor[pos2];
            vetor[pos2] = temp;
        }

        // Salvar em arquivo
        salvarVetor(vetor, String.format("vetor_crescente_%d_%dpercento.txt", tamanho, percentualDesordem));
    }

    // Gera vetor parcialmente desordenado em ordem decrescente
    private static void gerarVetorParcialmenteDecrescente(int tamanho, int percentualDesordem) {
        int[] vetor = new int[tamanho];

        // Preencher o vetor em ordem decrescente
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = tamanho - i - 1;
        }

        // Calcular quantos elementos serão desordenados
        int qtdDesordem = (int) (tamanho * (percentualDesordem / 100.0));
        Random random = new Random();

        // Desordenar a quantidade especificada de elementos
        for (int i = 0; i < qtdDesordem; i++) {
            int pos1 = random.nextInt(tamanho);
            int pos2 = random.nextInt(tamanho);
            int temp = vetor[pos1];
            vetor[pos1] = vetor[pos2];
            vetor[pos2] = temp;
        }

        // Salvar em arquivo
        salvarVetor(vetor, String.format("vetor_decrescente_%d_%dpercento.txt", tamanho, percentualDesordem));
    }

    // Gera vetor totalmente desordenado
    private static void gerarVetorTotalmenteDesordenado(int tamanho) {
        int[] vetor = new int[tamanho];

        // Preencher o vetor com números
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = i;
        }

        // Embaralhar completamente
        Random random = new Random();
        for (int i = tamanho - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = vetor[i];
            vetor[i] = vetor[j];
            vetor[j] = temp;
        }

        // Salvar em arquivo
        salvarVetor(vetor, String.format("vetor_desordenado_%d.txt", tamanho));
    }

    // Método para salvar o vetor em arquivo
    private static void salvarVetor(int[] vetor, String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            // Primeira linha contém o tamanho do vetor
            writer.println(vetor.length);

            // Segunda linha contém os elementos separados por espaço
            for (int i = 0; i < vetor.length; i++) {
                writer.print(vetor[i]);
                if (i < vetor.length - 1) {
                    writer.print(" ");
                }
            }

            System.out.println("Arquivo " + nomeArquivo + " criado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
}