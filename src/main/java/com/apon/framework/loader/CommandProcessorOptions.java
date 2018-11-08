package com.apon.framework.loader;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class CommandProcessorOptions {
    protected Options options;

    public CommandProcessorOptions() {
        options = new Options();
    }

    public CommandLineParser getCommandLineParser() {
        return new DefaultParser();
    }

    public Options getOptions() {
        return options;
    }
}
