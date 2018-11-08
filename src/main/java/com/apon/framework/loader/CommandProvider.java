package com.apon.framework.loader;

import com.apon.log.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private static Map<String, Class<? extends CommandProcessor>> commandToProcessorClass;
    private static Map<String, Class<? extends CommandProcessorOptions>> commandToOptionsClass;

    /**
     * Initialize variables and register all providers. Must be executed before other functions can be used.
     */
    public static void init() {
        commandToProcessorClass = new HashMap<>();
        commandToOptionsClass = new HashMap<>();

        registerAllAnnotatedProcessors();
    }

    /**
     * Get a CommandProcessor object that can process the given command.
     * @param command The command.
     */
    public static CommandProcessor getProcessor(String command) {
        if (!commandToProcessorClass.containsKey(command)) {
            Logger.logError("Could not find processor for command " + command + ".");
            return null;
        }

        Class<? extends CommandProcessor> processorClass = commandToProcessorClass.get(command);
        CommandProcessor commandProcessor = (CommandProcessor) getInstanceForClass(processorClass);
        if (commandProcessor == null) {
            // Logging needs to be done by calling code.
            return null;
        }

        // Set the options.
        Class<? extends CommandProcessorOptions> commandProcessorOptionsClass = commandToOptionsClass.get(command);
        CommandProcessorOptions commandProcessorOptions = (CommandProcessorOptions) getInstanceForClass(commandProcessorOptionsClass);
        commandProcessor.setCommandProcessorOptions(commandProcessorOptions);

        return commandProcessor;
    }

    /**
     * Go through all CommandProcessor classes that are annotated with Processor, and register them.
     */
    private static void registerAllAnnotatedProcessors() {
        Reflections reflections = new Reflections("com.apon.commands");

        for(Class<?> commandProcessorClass : reflections.getTypesAnnotatedWith(Processor.class)) {
            if (!CommandProcessor.class.isAssignableFrom(commandProcessorClass)) {
                Logger.logError("Annotation processor can only be used for classes that extend CommandProcessor. " +
                        commandProcessorClass.getCanonicalName() + " does not satisfy this criteria.");
                continue;
            }

            CommandProcessor commandProcessor = (CommandProcessor) getInstanceForClass(commandProcessorClass);
            if (commandProcessor == null) {
                Logger.logError("Got null instance as commandProcessor for class " + commandProcessorClass.getCanonicalName());
                continue;
            }
            registerProcessor(commandProcessor, commandProcessorClass.getAnnotation(Processor.class).options());
        }
    }

    /**
     * Register a CommandProcessor class together with its CommandProcessorOptions class.
     * @param commandProcessor The processor
     * @param commandProcessorOptionsClass The options class.
     */
    private static void registerProcessor(CommandProcessor commandProcessor, Class<? extends CommandProcessorOptions> commandProcessorOptionsClass) {
        if (commandToProcessorClass.containsKey(commandProcessor.getCommandName())) {
            // The command already exists, which should not happen. We don't overwrite the other command.
            Logger.logError("Command " + commandProcessor.getCommandName() + " already exists.");
            return;
        }

        commandToProcessorClass.put(commandProcessor.getCommandName(), commandProcessor.getClass());
        commandToOptionsClass.put(commandProcessor.getCommandName(), commandProcessorOptionsClass);
        Logger.logInfo("Registered command " + commandProcessor.getCommandName() + ".");
    }

    /**
     * Create an instance of a given class.
     * @param aClass Class type given.
     * @return Object, {@code null} if we could not instantiate the class.
     */
    private static Object getInstanceForClass(Class<?> aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Logger.logError("Could not instantiate class " + aClass.getCanonicalName() + ".");
            Logger.logError(e);
            return null;
        }
    }
}
