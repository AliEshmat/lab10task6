import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first 3 digits of your bank account: ");
        String userCode = scanner.nextLine();

        String url = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";
        String localFile = "plewibnra.txt";

        // Step 1: Download file
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new URL(url).openStream()))
        ) {
            FileWriter fw = new FileWriter(localFile);
            String line;

            while ((line = br.readLine()) != null) {
                fw.write(line + "\n");
            }
            fw.close();
            System.out.println("NBP file downloaded successfully.");

        } catch (IOException e) {
            System.out.println("Error downloading file.");
            e.printStackTrace();
            return;
        }

        // Step 2: Read file and search for bank code
        try (
                FileReader fr = new FileReader(localFile);
                BufferedReader br = new BufferedReader(fr)
        ) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith(userCode)) {
                    found = true;

                    // Example line: "101 PKO BANK POLSKI SA"
                    String[] parts = line.split("\\s+", 2);

                    String bankCode = parts[0];
                    String bankName = parts.length > 1 ? parts[1] : "Unknown bank name";

                    System.out.println("----- RESULT -----");
                    System.out.println("Abbreviated bank number: " + bankCode);
                    System.out.println("Bank name: " + bankName);
                    break;
                }
            }

            if (!found) {
                System.out.println("Bank with this code not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading local file.");
            e.printStackTrace();
        }
    }
}

