package com.apon.util;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static File getCurrentWorkingDirectory() {
        return Paths.get("").toFile().getAbsoluteFile();
    }

    public static List<File> getFilesInDirectory(File directory) {
        return Arrays.asList(directory.listFiles());
    }

}
