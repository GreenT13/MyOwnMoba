package com.apon.framework.loader;

import org.apache.commons.cli.CommandLine;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.io.File;

public abstract class CommandProcessor {
    // Default variables that need to be used for any program.
    protected TextIO textIO;
    protected TextTerminal<?> textTerminal;
    protected File currentDirectory;

    // The options class belonging to this processor.
    protected CommandProcessorOptions commandProcessorOptions;

    // The argument that is parsed from the command.
    protected CommandLine commandLine;

    public CommandProcessor() {
        // As default, no options are given.
        commandProcessorOptions = new CommandProcessorOptions();
    }

    public abstract void handleCommand();

    public abstract String getCommandName();

    public CommandProcessorOptions getCommandProcessorOptions() {
        return commandProcessorOptions;
    }

    public void setTextIO(TextIO textIO) {
        this.textIO = textIO;
    }

    public void setTextTerminal(TextTerminal<?> textTerminal) {
        this.textTerminal = textTerminal;
    }

    public void setCurrentDirectory(File currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void setCommandProcessorOptions(CommandProcessorOptions commandProcessorOptions) {
        this.commandProcessorOptions = commandProcessorOptions;
    }

    public void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
    }
}
