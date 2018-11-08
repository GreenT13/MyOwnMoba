package com.apon.commands.history;

import com.apon.Main;
import com.apon.framework.loader.CommandProcessor;
import com.apon.framework.loader.CommandProvider;

public class History extends CommandProcessor {

    static {
        CommandProvider.registerProcessor(new History());
    }

    @Override
    public void handleCommand() {
        int counter = 1;
        for (String command : Main.history) {
            textTerminal.println(counter + ". " + command);
            counter++;
        }
    }

    @Override
    public String getCommandName() {
        return "history";
    }
}
