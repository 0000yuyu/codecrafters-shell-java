import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (parts.length > 1)
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
            if(!inputCommand()) return;
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
        if (fullPath != null){
            List<String> fullArgs = new ArrayList<>();
            fullArgs.add(command.name); // 찾은 전체 경로를 첫 번째로 넣음
            fullArgs.addAll(Arrays.asList(command.args.split(" ")));
            try {
                ProcessBuilder pb = new ProcessBuilder(fullArgs);
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println(command.name + ": command not found");
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
                    System.out.println(command.args + " is a shell builtin");
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
    public boolean inputCommand()
    {
        System.out.print("$ ");
        String input = sc.nextLine().trim();
        if (input.startsWith("exit"))
            return false;
        this.command = new Command(input);
        return true;
    }
}
public class Main {
    public static void main(String[] args) throws Exception {
        CommandManager cm = new CommandManager();
        cm.run();
    }
}
