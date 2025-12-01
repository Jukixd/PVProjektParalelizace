1. POPIS PROJEKTU A CÍL

Tento program je určen k analíze počtu slov

Cílem je:
      1. Zpracovat velké soubory rychleji díky paralelismu.
      2. Spočítat frekvenci výskytu každého slova v souboru.


2. ARCHITEKTURA A SOUBĚŽNOST

   Projekt je rozdělen do rolí pro řešení problémů souběžnosti:

   A) TŘÍDA WORDCOUNTSERVICE:
      Řídí celý proces, inicializuje zdroje a vlákna. Obsahuje hlavní logiku.

   B) TŘÍDA FILEREADERTASK:
      Čte řádky ze souboru a vkládá je do fronty.

   C) TŘÍDA WORDANALYZERWORKER:
      Pool 4 vláken (Workerů) odebírá řádky z fronty, zpracovává je a aktualizuje výsledky.

   D) TŘÍDA INPUTVALIDATOR:
      Stará se o získání a validaci platné cesty k souboru z konzole.

3. POUŽITÍ A SPUŠTĚÍ

Před spuštěním zkompilujte všechny .java soubory:
 javac *.java

Spuštění programu:
 java MainApp

Interakce:
   Program vás vyzve k zadání cesty k souboru. Program se neukončí při chybné cestě, ale bude vyžadovat opakované zadání (díky třídě InputValidator).

Příklad vstupu:
   C:\cesta\k\vasemu\souboru.txt
