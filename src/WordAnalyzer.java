import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class WordAnalyzer implements Runnable {
    private final BlockingQueue<String> queue;
    private final ConcurrentHashMap<String, Long> wordCounts;
    private final String poisonPill;

    public WordAnalyzer(BlockingQueue<String> queue, ConcurrentHashMap<String, Long> wordCounts, String poisonPill) {
        this.queue = queue;
        this.wordCounts = wordCounts;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Worker " + threadName + " spuštěn.");

        try {
            while  (true) {
                String line = queue.take();
                if (line.equals(poisonPill)) {

                    queue.put(poisonPill);
                    break;
                }


                String[] words = line.toLowerCase().split("[\\p{Punct}\\s]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCounts.merge(word, 1L, Long::sum);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Worker " + threadName + " ukončen.");
    }
}