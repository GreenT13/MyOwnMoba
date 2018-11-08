package com.apon.commands.ls;

import com.apon.framework.loader.CommandProcessorOptions;
import org.apache.commons.cli.Option;

public class ListFilesOptions extends CommandProcessorOptions {
    protected final static String ORDER = "o";

    public ListFilesOptions() {
        super();
        options.addOption(Option.builder(ORDER)
                .required(false)
                .hasArg(false)
                .desc("Print directories first, then files.")
                .build());
    }
}
