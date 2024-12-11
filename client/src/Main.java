import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to The File Store Service");

        while(true) {
            System.out.println("> ");

            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting The File Store Service...");
                break;
            }

            try {
                new ProcessCommand().processCommand(command);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}