package TextAnalyser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TextProcessor {

    private Scanner scannerWords;

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(new File(TextProcessor.class.getResource("text.txt").getPath().substring(1)));
        Scanner scanner2 = new Scanner(new File(TextProcessor.class.getResource("censored.txt").getPath().substring(1)));
        while (scanner2.hasNext()) {
            words.add(scanner2.next().toLowerCase());
        }
        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine().toLowerCase();
            for (String cenWord: words){
                temp = temp.replaceAll(cenWord, "*".repeat(cenWord.length()));
            }
            try (FileWriter writer = new FileWriter("src/output.txt", true)) {
                writer.write(temp);
                writer.write("\n");
                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public TextProcessor() {
        try {
            scannerWords = new Scanner(new File(TextProcessor.class.getResource("censored.txt").getPath().substring(1)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void processText(File file) throws FileNotFoundException {
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scannerWords.hasNext()) {
            words.add(scannerWords.next().toLowerCase());
        }
        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine().toLowerCase();
            for (String cenWord: words){
                temp = temp.replaceAll(cenWord, "*".repeat(cenWord.length()));
            }
            try (FileWriter writer = new FileWriter("D://result/"+file.getName(), true)) {
                writer.write(temp);
                writer.write("\n");
                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
