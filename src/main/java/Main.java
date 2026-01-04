import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String command = sc.nextLine().trim();
            if (command.equals("exit"))
                break;
            if (command.startsWith("echo"))
            {
                String sentence = command.substring(5);
                System.out.println(sentence);
            }
            else 
                System.out.println(command + ": command not found");
        }
    }
}
