import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public class FileReaderTask implements Runnable {
    private final Path filePath;
    private final BlockingQueue<String> queue;
    private final String poisonPill;
    private final int numWorkers;

    public FileReaderTask(Path filePath, BlockingQueue<String> queue, String poisonPill, int numWorkers) {
        this.filePath = filePath;
        this.queue = queue;
        this.poisonPill = poisonPill;
        this.numWorkers = numWorkers;
    }

    @Override
    public void run() {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                queue.put(line);
            }
        } catch (IOException e) {
            System.err.println("Chyba při čtení souboru: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                for (int i = 0; i < numWorkers; i++) {
                    queue.put(poisonPill);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}