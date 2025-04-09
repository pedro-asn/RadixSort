import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class RadixSortArtigo {

    public static void radixSort(int[] vetA, int size) {
        int[] temp = new int[size];
        int[] count = new int[10];
        int max = pegaMax(vetA, size);
        int expo = 1;

        while (max / expo > 0) {
            for (int i = 0; i < 10; i++) {
                count[i] = 0;
            }

            for (int i = 0; i < size; i++) {
                count[(vetA[i] / expo) % 10]++;
            }

            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            for (int i = size - 1; i >= 0; i--) {
                temp[count[(vetA[i] / expo) % 10] - 1] = vetA[i];
                count[(vetA[i] / expo) % 10]--;
            }

            for (int i = 0; i < size; i++) {
                vetA[i] = temp[i];
            }

            expo *= 10;
        }
    }

    private static int pegaMax(int[] vetA, int size) {
        int max = vetA[0];
        for (int i = 1; i < size; i++) {
            if (vetA[i] > max) {
                max = vetA[i];
            }
        }
        return max;
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

    public static void main(String[] args) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String resultadosDir = "resultados_codigo_artigo" + timeStamp;
        new File(resultadosDir).mkdir();

        String relatorioPath = resultadosDir + "/relatorio_tempos_codigo_artigo.txt";
        List<String> resultados = new ArrayList<>();
        resultados.add("RELATÓRIO DE EXECUÇÃO - RADIXSORT");
        resultados.add("Data e hora: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        resultados.add("\nTEMPOS DE EXECUÇÃO:");
        resultados.add("----------------------------------------");

        File diretorio = new File(".");
        File[] arquivos = diretorio.listFiles((dir, name) -> name.startsWith("vetor_") && name.endsWith(".txt"));

        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum arquivo de vetor encontrado no diretório atual.");
            return;
        }

        for (File arquivo : arquivos) {
            try {
                int[] vetor = lerVetor(arquivo);
                System.out.println("Processando arquivo: " + arquivo.getName());

                long inicio = System.nanoTime();
                radixSort(vetor, vetor.length);
                long fim = System.nanoTime();

                double tempoMs = (fim - inicio) / 1_000_000.0;

                String nomeArquivoOrdenado = "ordenado_" + arquivo.getName();
                String caminhoCompleto = resultadosDir + "/" + nomeArquivoOrdenado;

                salvarVetor(vetor, caminhoCompleto);

                String resultado = String.format("Arquivo: %s\n" +
                                "Tempo de execução: %.3f ms\n" +
                                "Arquivo de saída: %s\n" +
                                "----------------------------------------",
                        arquivo.getName(),
                        tempoMs,
                        nomeArquivoOrdenado);
                resultados.add(resultado);

                System.out.println("Arquivo ordenado salvo como: " + nomeArquivoOrdenado);
                System.out.println("Tempo de execução: " + String.format("%.3f ms", tempoMs));
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
}