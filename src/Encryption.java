import java.io.*;
import java.util.Scanner;

public class Encryption {


    private static final char[] alphabet = new char[]{
            'а', 'б', 'в', 'г', 'ґ', 'д', 'е',
            'є', 'ж', 'з', 'и', 'і', 'ї', 'й', 'к', 'л', 'м',
            'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц',
            'ч', 'ш', 'щ', 'ь', 'ю', 'я', '.', ',', '”', '”', ':',
            '-', '!', '?', ' '};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int selectionNumber = 0;
        while (selectionNumber != 1 && selectionNumber != 2) {
            System.out.println("Щоб шифрувати файл, натисніть \"1\":");
            System.out.println("Щоб розшифрувати файл, натисніть \"2\":");

            selectionNumber = scanner.nextInt();
            scanner.nextLine();

            if (selectionNumber != 1 && selectionNumber != 2) {
                System.out.println("Неправильне число. Будь ласка, введіть 1 або 2.");
            }
        }

        try {
            System.out.print("Введіть шлях до файлу: ");
            String filePath = scanner.nextLine();

            if (endsWithTxt(filePath)) {
                if (selectionNumber == 1) {
                    System.out.print("Введіть крок для шифрування: ");
                    int shift = scanner.nextInt();
                    scanner.nextLine();

                    String encryptedContent = encryptFile(filePath, shift);
                    System.out.print("Введіть шлях для збереження зашифрованого файлу: ");
                    String encryptedFilePath = scanner.nextLine();
                    saveToFile(encryptedFilePath, encryptedContent);
                    System.out.println("Файл успішно зашифрований та збережений.");
                } else if (selectionNumber == 2) {
                        System.out.println("Дешифрувати файл за допомою кроку натисніть \"1\":");
                        System.out.println("Дешифрувати файл автоматично натисніть \"2\":");
                        int decryptSelectionNumber = 0;
                        while (decryptSelectionNumber != 1 && decryptSelectionNumber != 2) {
                            decryptSelectionNumber = scanner.nextInt();
                        if (decryptSelectionNumber == 1) {
                            System.out.print("Введіть крок для шифрування: ");
                            int shift = scanner.nextInt();
                            scanner.nextLine();
                            String encryptedContent = decryptFileThroughStep(filePath, shift);
                            System.out.print("Введіть шлях для збереження зашифрованого файлу: ");

                            String encryptedFilePath = scanner.nextLine();
                            saveToFile(encryptedFilePath, encryptedContent);
                            System.out.println("Файл успішно дешифровано та збережений.");
                        } else if (decryptSelectionNumber == 2) {
                            String decryptedContent = autoDecryptFile(filePath);
                            System.out.print("Введіть шлях для збереження автоматично дешифрованого файлу: ");
                            scanner.nextLine();
                            String decryptedFilePath = scanner.nextLine();
                            saveToFile(decryptedFilePath, decryptedContent);
                            System.out.println("Файл успішно автоматично дешифровано та збережений.");
                        }
                        if (decryptSelectionNumber != 1 && decryptSelectionNumber != 2) {
                            System.out.println("Неправильне число. Будь ласка, введіть 1 або 2.");
                        }
                    }
                }
            } else {
                System.out.println("Неправильне розширення файлу. Підтримується лише .txt");
            }
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }

        scanner.close();
    }

    public static boolean endsWithTxt(String filePath) {
        return filePath.endsWith(".txt");
    }

    public static String encryptFile(String filePath, int step) {
        StringBuilder sb = new StringBuilder();
        String str;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return caesarCipher(sb.toString(), step);
    }

    public static String autoDecryptFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        String str;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int bestShift = findRightStep(sb.toString());
        return caesarCipher(sb.toString(), -bestShift);
    }
    public static String decryptFileThroughStep(String filePath, int step) {
        StringBuilder sb = new StringBuilder();
        String str;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return caesarCipher(sb.toString(), -step);
    }

    public static int findRightStep(String text) {
        int step = 0;
        int maxValidWords = 0;

        for (int shift = 1; shift <= alphabet.length - 1; shift++) {
            String decryptedText = caesarCipher(text, -shift);
            int validWordsCount = countValidWords(decryptedText);

            if (validWordsCount > maxValidWords) {
                maxValidWords = validWordsCount;
                step = shift;
            }
        }

        return step;
    }

    public static String caesarCipher(String text, int shift) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char character : text.toCharArray()) {
            char encryptedChar = character;
            for (int i = 0; i < alphabet.length; i++) {
                if (Character.toLowerCase(character) == alphabet[i]) {
                    int index = (i + shift + alphabet.length) % alphabet.length;
                    encryptedChar = alphabet[index];
                    break;
                }
            }
            stringBuilder.append(encryptedChar);
        }

        return stringBuilder.toString();
    }

    public static void saveToFile(String path, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(path);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
        }
    }

    public static int countValidWords(String text) {
        int count = 0;
        for (String word : Dictionary.WORDS) {
            if (text.toLowerCase().contains(word)) {
                count++;
            }
        }
        return count;
    }
}