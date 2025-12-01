import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.Map;

public class WordCounter {

    private static final int NUM_WORKERS = 4;
    private static final String POISON_PILL = "END_OF_FILE_SIGNAL";
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);
    private final ConcurrentHashMap<String, Long> wordCounts = new ConcurrentHashMap<>();

    public void runAnalysisLoop() {
        Path inputFile = InputValidator.getValidFilePath();
        analyzeFile(inputFile);
    }

    private void analyzeFile(Path inputFile) {
        System.out.println("Spouštím paralelní analýzu souboru: " + inputFile.getFileName());
        ExecutorService workerPool = Executors.newFixedThreadPool(NUM_WORKERS);
        for (int i = 0; i < NUM_WORKERS; i++) {
            workerPool.execute(new WordAnalyzer(queue, wordCounts, POISON_PILL));
        }
        FileReaderTask fileReader = new FileReaderTask(inputFile, queue, POISON_PILL, NUM_WORKERS);
        Thread producerThread = new Thread(fileReader);
        producerThread.start();

        try {
            producerThread.join();
            workerPool.shutdown();
            workerPool.awaitTermination(30, TimeUnit.SECONDS); // Čekání na workery
            displayResults();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Analýza byla přerušena.");
        }
    }
    private void displayResults() {
        System.out.println("\n--- Finální Výsledky Analýzy ---");
        if (wordCounts.isEmpty()) {
            System.out.println("Soubor neobsahuje žádná slova nebo došlo k chybě zpracování.");
        } else {
            wordCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.printf("Slovo: %-15s | Počet: %d\n", entry.getKey(), entry.getValue()));
            System.out.println("\nAnalýza dokončena.");
        }
    }
}