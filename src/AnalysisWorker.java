import javax.swing.*;
import java.io.File;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class AnalysisWorker extends SwingWorker<Map<String, Long>, Void> {

    private final File file;
    private final JTextArea resultsArea;
    private final JLabel statusLabel;
    private final int numWorkers;
    private final String poisonPill;
    private final JButton startButton;

    public AnalysisWorker(File file, JTextArea resultsArea, JLabel statusLabel, int numWorkers, String poisonPill, JButton startButton) {
        this.file = file;
        this.resultsArea = resultsArea;
        this.statusLabel = statusLabel;
        this.numWorkers = numWorkers;
        this.poisonPill = poisonPill;
        this.startButton = startButton;
    }

    @Override
    protected Map<String, Long> doInBackground() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);
        ConcurrentHashMap<String, Long> wordCounts = new ConcurrentHashMap<>();

        ExecutorService workerPool = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            workerPool.execute(new WordAnalyzer(queue, wordCounts, poisonPill));
        }
        FileReaderTask fileReader = new FileReaderTask(file.toPath(), queue, poisonPill, numWorkers);
        new Thread(fileReader).start();

        new Thread(fileReader).join();
        workerPool.shutdown();
        workerPool.awaitTermination(30, TimeUnit.SECONDS);

        return wordCounts;
    }

    @Override
    protected void done() {
        try {
            Map<String, Long> finalResults = get();

            String results = finalResults.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .map(entry -> String.format("Slovo: %-15s | Počet: %d", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("\n"));

            resultsArea.setText(results);
            statusLabel.setText("Stav: Dokončeno. Celkem unikátních slov: " + finalResults.size());

        } catch (InterruptedException | ExecutionException e) {
            statusLabel.setText("Stav: Chyba při zpracování: " + e.getMessage());
            resultsArea.setText("Analýza selhala.");
        } finally {
            startButton.setEnabled(true);
        }
    }
}