package org.gitrust.fileindexer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Command {
    private String command;
    private String[] arguments;

    public Command(String command) {
        this(command, new String[]{});
    }

    public String getFirstArgument() {
        if (this.arguments != null && this.arguments.length > 0) {
            return this.arguments[0];
        }
        return null;
    }
}
