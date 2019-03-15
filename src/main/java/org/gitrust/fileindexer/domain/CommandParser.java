package org.gitrust.fileindexer.domain;

import java.util.Arrays;

public class CommandParser {

    public Command parseCommand(String commandLine) {
        // TODO enumerate commands
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return new Command("");
        } else if (commandLine.trim().toLowerCase().startsWith("exit")) {
            return new Command("exit");
        } else if (commandLine.trim().toLowerCase().startsWith("q ")) {
            return new Command("q", parseArguments(commandLine));
        }

        return new Command("");
    }

    private String[] parseArguments(String cmd) {
        // TODO improve command argument parsing
        if (cmd != null && !cmd.trim().isEmpty()) {
            String[] args = cmd.trim().split(" ");
            if (args != null && args.length > 1) {
                return Arrays.copyOfRange(args, 1, args.length);
            }
        }
        return new String[]{};
    }
}
