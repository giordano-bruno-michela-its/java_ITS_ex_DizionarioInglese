package controller;

import model.Definizione;

import java.io.*;
import java.util.*;

public class QuizController {
    private final List<Definizione> definizioni = new ArrayList<>();
    private final Random random = new Random();
    private int risposteCorrette = 0;
    private int risposteErrate = 0;

    public QuizController() {
        caricaDefinizioni("dict-ita-eng.tsv");
    }

    private void caricaDefinizioni(String filePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filePath), "UTF-8"))) {
            if (reader == null) {
                throw new FileNotFoundException("File non trovato: " + filePath);
            }
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String vocaboloInglese = parts[1];
                    String traduzioneItaliana = parts[2];
                    String fraseInglese = parts[3];
                    String traduzioneFrase = parts[4];
                    definizioni.add(new Definizione(id, vocaboloInglese, traduzioneItaliana, fraseInglese, traduzioneFrase));
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file: " + e.getMessage());
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n== Benvenuto al Quiz Dizionario Inglese ==\n");
            System.out.println("1) Modalità Apprendimento");
            System.out.println("2) Modalità Quiz");
            System.out.println("3) Esci");
            System.out.print("\nScegli un'opzione: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    startLearningSession();
                    break;
                case "2":
                    startQuiz();
                    break;
                case "3":
                    System.out.println("Grazie per aver utilizzato il programma!");
                    return;
                default:
                    System.out.println("\nScelta non valida. Riprova.");
            }
        }
    }

    public void startLearningSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== Modalità Scheda di Apprendimento ==\n");
        while (true) {
            Definizione definizione = definizioni.get(random.nextInt(definizioni.size()));
            System.out.println("Vocabolo: " + definizione.getVocaboloInglese());
            System.out.println("Traduzione: " + definizione.getTraduzioneItaliana());
            System.out.println("Frase: " + definizione.getFraseInglese());
            System.out.println("Traduzione Frase: " + definizione.getTraduzioneFrase());
            System.out.print("\nVuoi continuare? (s/n): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("n")) break;
        }
    }

    public void startQuiz() {
        Scanner scanner = new Scanner(System.in);
        List<String> output = new ArrayList<>();
        risposteCorrette = 0;
        risposteErrate = 0;

        System.out.println("== Modalità Quiz ==\n");
        System.out.println("Digita '0' in qualsiasi momento per tornare al menu principale.");
        for (Definizione definizione : definizioni) {
            System.out.println("Qual è la traduzione di \"" + definizione.getVocaboloInglese() + "\"?\n");
            List<String> opzioni = new ArrayList<>(List.of(definizione.getTraduzioneItaliana(), "opzione errata 1", "opzione errata 2"));
            Collections.shuffle(opzioni);
            for (int i = 0; i < opzioni.size(); i++) {
                System.out.println((i + 1) + ") " + opzioni.get(i));
            }
            System.out.print("\nRisposta: ");
            int risposta;

            try {
                risposta = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Inserisci un numero valido!");
                continue;
            }

            if (risposta == 0) {
                System.out.println("Tornando al menu principale...");
                break;
            }

            if (risposta < 1 || risposta > opzioni.size()) {
                System.out.println("Opzione non valida. Riprova.");
                continue;
            }

            scanner.nextLine();
            boolean corretto = opzioni.get(risposta - 1).equals(definizione.getTraduzioneItaliana());
            if (corretto) {
                risposteCorrette++;
                System.out.println("Corretto!\n");
            } else {
                risposteErrate++;
                System.out.println("Sbagliato!");
            }
            System.out.println("Risposte corrette: " + risposteCorrette + ", Risposte sbagliate: " + risposteErrate);
            System.out.println("Digita '0' in qualsiasi momento per tornare al menu principale.\n");
            output.add("Domanda: Qual è la traduzione di \"" + definizione.getVocaboloInglese() + "\"?");
            output.add("Risposta utente: " + opzioni.get(risposta - 1));
            output.add("Esito: " + (corretto ? "CORRETTO" : "SBAGLIATO"));
        }
        output.add("Punteggio totale: " + risposteCorrette + "/" + (risposteCorrette + risposteErrate));
        scriviOutput(output);
    }

    private void scriviOutput(List<String> output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("risultati.txt"))) {
            for (String line : output) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }
}