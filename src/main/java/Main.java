import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

class Command {
    String name;
    String args;
    static List<String> builtins = List.of("echo", "exit", "type");
    public Command(String input)
    {
        String[] parts = input.split("\\s+", 2);
        this.name = parts[0];
        this.args = parts[1];
    }
    public boolean isBuiltin(String commandString)
    {
        return builtins.contains(commandString);
    }
}
class CommandManager {
    Scanner sc;
    Command command;
    // 커맨드 입력
    public void run()
    {
        sc = new Scanner(System.in);
        while(true)
        {
            inputCommand();
            if (command.name.equals("exit"))
                return ;
            processCommand();
        }
    }
    public void processCommand()
    {
        if (command.isBuiltin(command.name))
            processBuiltinCommand();
        else processExcuteCommand();
    }
    private void processExcuteCommand() {
        String fullPath = getExecutableFullPath(command.name);
        String[] args = command.args.split(" ");

        if (fullPath != null){
            try {
                ProcessBuilder pb = new ProcessBuilder(args);
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println(command + ": command not found");
        }
    }
    public void processBuiltinCommand()
    {
        switch (command.name) {
            case "echo":
                System.out.println(command.args);
                break;
            case "type":
                String fullpath;
                if (command.isBuiltin(command.args))
                    System.out.println(command.name + " is a shell builtin");
                else if ((fullpath = getExecutableFullPath(command.args)) != null) {
                    System.out.println(command.args + " is " + fullpath);
                }
                else {
                    System.out.println(command.args+": not found");
                }
                break;
        }
    }
    public String getExecutableFullPath(String command)
    {
        String pathVariable = System.getenv("PATH");
        String[] paths = pathVariable.split(":");

        for(String directory : paths) {
            Path fullpath = Paths.get(directory).resolve(command);
            if (Files.exists(fullpath) && Files.isExecutable(fullpath)) {
            return fullpath.toString();
            }
        }
        return null;
    }
    public void inputCommand()
    {
        System.out.print("$ ");
        String input = sc.nextLine().trim();
        this.command = new Command(input);
    }
}
public class Main {
    public static void main(String[] args) throws Exception {
        CommandManager cm = new CommandManager();
        cm.run();
    }
}
