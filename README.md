## 1. POPIS PROJEKTU A CÍL

Tato aplikace řeší reálný problém zpracování velkých textových 
dat pomocí paralelního programování. Umožňuje uživateli vybrat 
textový soubor a efektivně spočítat výskyt jednotlivých slov s 
využitím více vláken současně

Cílem je:
* Zpracovat velké soubory rychleji díky paralelismu.
* Spočítat frekvenci výskytu každého slova v souboru.

## 2. ARCHITEKTURA

   Projekt je rozdělen do rolí pro řešení problémů souběžnosti:

* Main.java - Vstupní bod aplikace.
* WordCounterFrame.java - Uživatelské rozhraní (Swing).
* AnalysisWorker.java - SwingWorker pro spouštění úlohy na pozadí.
* FileReaderTask.java - Vlákno producenta (čtení souboru).
* WordAnalyzer.java - Vlákno konzumenta (zpracování textu).
## 3. POUŽITÍ A SPUŠTĚÍ

Před spuštěním zkompilujte všechny .java soubory:
javac *.java

Spuštění programu:
 java Main.java


