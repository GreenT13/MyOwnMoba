package com.apon.commands.ls;

import com.apon.framework.loader.CommandProcessor;
import com.apon.framework.loader.CommandProvider;
import com.apon.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.util.Comparator;
import java.util.List;

public class ListFiles extends CommandProcessor {

    static {
        CommandProvider.registerProcessor(new ListFiles());
    }

    public ListFiles() {
        commandProcessorOptions = new ListFilesOptions();
    }

    @Override
    public void handleCommand() {
        // Get list of files inside current directory.
        List<File> files = FileUtil.getFilesInDirectory(currentDirectory);

        // If option to sort if given, sort files by first directories (alphabetically) and then files (alphabetically).
        if (commandLine.hasOption("o")) {
            files.sort(new OrderFiles());
        }

        for (File file : files) {
            if (file.isDirectory()) {
                textTerminal.executeWithPropertiesConfigurator(
                        props -> props.setPromptColor(Color.GREEN),
                        t -> t.println(file.getName()));
            } else {
                textTerminal.executeWithPropertiesConfigurator(
                        props -> props.setPromptColor(Color.CYAN),
                        t -> t.println(file.getName()));
            }
        }

    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    private class OrderFiles implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() != o2.isDirectory()) {
                return o1.isDirectory() ? -1 : 1;
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

}
