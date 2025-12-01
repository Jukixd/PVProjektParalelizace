import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

public class InputValidator {
    public static Path getValidFilePath() {
        Scanner scanner = new Scanner(System.in);
        Path inputFile = null;
        boolean isValid = false;

        System.out.println("--- Paralelní Analyzátor Textových Souborů ---");

        while (!isValid) {
            System.out.print("Zadejte plnou cestu k textovému souboru (.txt) nebo 'exit' pro ukončení: ");
            String inputPath = scanner.nextLine().trim();





            if (inputPath.equalsIgnoreCase("exit")) {
                System.out.println("Program byl ukončen.");
                System.exit(0);
            }

            File file = new File(inputPath);

            if (!file.exists()) {
                System.err.println("Chyba: Soubor na zadané cestě neexistuje.");
            } else if (file.isDirectory()) {
                System.err.println("Chyba: Zadaná cesta je adresář, nikoli soubor.");
            } else {
                inputFile = file.toPath();
                isValid = true;
            }
        }
        return inputFile;
    }
}