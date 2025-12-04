import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.*;

public class WordCounterFrame extends JFrame {

    private final JTextArea resultsArea = new JTextArea(15, 40);
    private final JLabel statusLabel = new JLabel("Stav: Připraveno");
    private File selectedFile;
    private JButton startButton;

    private static final int NUM_WORKERS = 4;
    private static final String POISON_PILL = "END_OF_FILE_SIGNAL";

    public WordCounterFrame() {
        super("Paralelní Počítadlo Slov");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupGUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupGUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        JButton selectButton = new JButton("Vybrat soubor (.txt)");
        startButton = new JButton("Spustit analýzu");

        selectButton.addActionListener(e -> selectFile());
        startButton.addActionListener(e -> startAnalysis());

        topPanel.add(selectButton);
        topPanel.add(startButton);

        resultsArea.setEditable(false);
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(statusLabel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Stav: Soubor vybrán: " + selectedFile.getName());
            resultsArea.setText("");
        }
    }

    private void startAnalysis() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Prosím, nejprve vyberte soubor.", "Chyba", JOptionPane.WARNING_MESSAGE);
            return;
        }
        startButton.setEnabled(false);
        statusLabel.setText("Stav: Zpracovávám, čekejte...");

        new AnalysisWorker(selectedFile, resultsArea, statusLabel, NUM_WORKERS, POISON_PILL, startButton).execute();
    }
}