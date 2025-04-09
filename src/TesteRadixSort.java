import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class TesteRadixSort {
    public static void main(String[] args) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String resultadosDir = "resultados_" + timeStamp;
        new File(resultadosDir).mkdir();

        String relatorioPath = resultadosDir + "/relatorio_tempos.txt";
        List<String> resultados = new ArrayList<>();
        resultados.add("RELATÓRIO DE EXECUÇÃO - RADIXSORT");
        resultados.add("Data e hora: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        resultados.add("\nTEMPOS DE EXECUÇÃO:");
        resultados.add("----------------------------------------");

        File diretorio = new File(".");
        File[] arquivos = diretorio.listFiles((dir, name) -> name.startsWith("vetor_") && name.endsWith(".txt"));

        for (File arquivo : arquivos) {
            try {
                int[] vetor = lerVetor(arquivo);
                String nomeOriginal = arquivo.getName();

                long inicio = System.nanoTime();
                radixSort(vetor);
                long fim = System.nanoTime();

                double tempoMs = (fim - inicio) / 1_000_000.0;

                String nomeArquivoOrdenado = "ordenado_" + nomeOriginal;
                String caminhoCompleto = resultadosDir + "/" + nomeArquivoOrdenado;

                salvarVetor(vetor, caminhoCompleto);

                String resultado = String.format("Arquivo: %s\n" +
                                "Tempo de execução: %.3f ms\n" +
                                "Arquivo de saída: %s\n" +
                                "----------------------------------------",
                        nomeOriginal,
                        tempoMs,
                        nomeArquivoOrdenado);

                resultados.add(resultado);

                System.out.println("Processado: " + nomeOriginal);
                System.out.println("Tempo: " + String.format("%.3f ms", tempoMs));
                System.out.println("----------------------------------------");

            } catch (IOException e) {
                String erro = "Erro ao processar arquivo " + arquivo.getName() + ": " + e.getMessage();
                resultados.add(erro);
                System.err.println(erro);
            }
        }

        try {
            Files.write(Paths.get(relatorioPath), resultados, StandardCharsets.UTF_8);
            System.out.println("\nRelatório salvo em: " + relatorioPath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }

    private static int[] lerVetor(File arquivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            int tamanho = Integer.parseInt(reader.readLine());
            int[] vetor = new int[tamanho];

            String[] elementos = reader.readLine().split(" ");
            for (int i = 0; i < tamanho; i++) {
                vetor[i] = Integer.parseInt(elementos[i]);
            }

            return vetor;
        }
    }

    private static void salvarVetor(int[] vetor, String nomeArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println(vetor.length);

            for (int i = 0; i < vetor.length; i++) {
                writer.print(vetor[i]);
                if (i < vetor.length - 1) {
                    writer.print(" ");
                }
            }
        }
    }

    private static void radixSort(int[] array) {
        int max = Arrays.stream(array).max().orElse(0);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(array, exp);
        }
    }

    private static void countingSort(int[] array, int exp) {
        int n = array.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) {
            count[(array[i] / exp) % 10]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(array[i] / exp) % 10] - 1] = array[i];
            count[(array[i] / exp) % 10]--;
        }

        System.arraycopy(output, 0, array, 0, n);
    }
}