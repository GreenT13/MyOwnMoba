package com.apon.framework.loader;

import com.apon.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private static Map<String, Class<? extends CommandProcessor>> commandToProcessorClass = new HashMap<>();

    public static void registerProcessor(CommandProcessor commandProcessor) {
        if (commandToProcessorClass.containsKey(commandProcessor.getCommandName())) {
            // The command already exists, which should not happen. We don't overwrite the other command.
            Logger.logError("Command " + commandProcessor.getCommandName() + " already exists.");
            return;
        }

        commandToProcessorClass.put(commandProcessor.getCommandName(), commandProcessor.getClass());
        Logger.logInfo("Registered command " + commandProcessor.getCommandName() + ".");
    }

    public static CommandProcessor getProcessor(String command) {
        if (!commandToProcessorClass.containsKey(command)) {
            Logger.logError("Could not find processor for command " + command + ".");
            return null;
        }

        Class<? extends CommandProcessor> processorClass = commandToProcessorClass.get(command);


        try {
            return processorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.logError("Could not initialize processor for command " + command + ".");
            Logger.logError(e);
            return null;
        }
    }
}
