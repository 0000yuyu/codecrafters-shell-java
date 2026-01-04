import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static boolean isValidCommand(String command)
    {
        String pathVariable = System.getenv("PATH");
        String[] paths = pathVariable.split(":");
        boolean found = false;

        for(String directory : paths) {
            Path fullpath = Paths.get(directory).resolve(command);
            if (Files.exists(fullpath) && Files.isExecutable(fullpath)) {
            System.out.println(command + " is " + fullpath);
            found = true;
            break;
            }
        }
        return found;
    }
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = sc.nextLine().trim();
            String[] commands = input.split("\\s+", 2);
            switch (commands[0]) {
                case "exit":
                    return;
                case "echo":
                    System.out.println(commands[1]);
                    break;
                case "type":
                    List<String> builtins = List.of("echo", "exit", "type");
                    if (commands.length > 1 && builtins.contains(commands[1])){
                        System.out.println(commands[1] + " is a shell builtin");
                    }
                    else if (!isValidCommand(commands[1]))
                        System.out.println(commands[1]+": not found");
                    break;
                default:
                    if (commands.length > 1)
                        commands[0] = commands[1];
                    System.out.println(commands[0]+ ": command not found");
            }
        }
    }
}
