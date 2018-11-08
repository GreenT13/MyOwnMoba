package com.apon;

import com.apon.commands.history.History;
import com.apon.commands.ls.ListFiles;
import com.apon.framework.loader.CommandProcessor;
import com.apon.framework.loader.CommandProcessorOptions;
import com.apon.framework.loader.CommandProvider;
import com.apon.framework.swing.FancyCaret;
import com.apon.log.Logger;
import com.apon.util.FileUtil;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.beryx.textio.*;
import org.beryx.textio.swing.SwingTextTerminal;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Workaround :)
        // https://stackoverflow.com/questions/3560103/how-to-force-a-class-to-be-initialised
        try {
            Class.forName(ListFiles.class.getName(), true, ListFiles.class.getClassLoader());
            Class.forName(History.class.getName(), true, History.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Main main = new Main();
        main.run();
    }

    private static final String EXIT_INTERRUPT_KEY = "ctrl Q";

    private File currentDirectory;

    // History needs to be public so that history command can get it.
    public static ArrayList<String> history;

    private Main() {
        currentDirectory = FileUtil.getCurrentWorkingDirectory();
        history = new ArrayList<>();
    }

    private void run() {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal<?> terminal = textIO.getTextTerminal();

        // Use cool terminal caret.
        if (terminal instanceof SwingTextTerminal) {
            ((SwingTextTerminal) terminal).getTextPane().setCaret(new FancyCaret());
        }

        // Make sure copy paste command is available.
        // TODO: apparently this doesnt work. Fix it.
        terminal.getProperties().put("user.interrupt.key", EXIT_INTERRUPT_KEY);

        terminal.registerHandler(KeyStroke.getKeyStroke("UP").toString(), t -> {
            terminal.println("x");
            return new ReadHandlerData(ReadInterruptionStrategy.Action.CONTINUE);
        });

        while (true) {
            // Other commands can reset color etc, so we set default again.
            terminal.getProperties().setPromptColor(Color.GREEN);

            terminal.print("[" + currentDirectory.getName() + "] >");
            String command = textIO.newStringInputReader().withMinLength(0).read();

            // Parse the commandName, to find the correct processor.
            String commandName;
            if (!command.contains(" ")) {
                commandName = command;
            } else {
                commandName = command.trim().substring(0,command.indexOf(" "));
            }

            if (commandName.length() == 0) {
                continue;
            }

            // Add command to history.
            history.add(command);

            CommandProcessor commandProcessor = CommandProvider.getProcessor(commandName);

            if (commandProcessor == null) {
                terminal.println("Could not find processor for command " + commandName + ".");
                continue;
            }

            // Set default variables
            commandProcessor.setTextIO(textIO);
            commandProcessor.setTextTerminal(terminal);
            commandProcessor.setCurrentDirectory(currentDirectory);

            // Parse the argument list.
            String arguments = command.trim().substring(command.indexOf(" ") + 1);
            CommandProcessorOptions commandProcessorOptions = commandProcessor.getCommandProcessorOptions();
            CommandLineParser commandLineParser = commandProcessorOptions.getCommandLineParser();
            try {
                commandProcessor.setCommandLine(commandLineParser.parse(commandProcessorOptions.getOptions(), arguments.split(" ")));
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            try {
                commandProcessor.handleCommand();
            } catch (Exception e) {
                terminal.println("Some unhandled exception occurred in the processor.");
                Logger.logError(e);
            }
        }
    }

}
