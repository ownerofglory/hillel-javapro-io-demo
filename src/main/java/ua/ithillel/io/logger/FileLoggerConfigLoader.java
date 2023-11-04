package ua.ithillel.io.logger;

import java.io.File;
import java.io.InputStream;

public class FileLoggerConfigLoader {
    public static LoggerConfig load(String string) {
        // check if does not exist
//        return new EmptyConfig();


        return FileLoggerConfig.builder()
                .setPath("")
                .setFormat("")
                .build();
    }
    public static LoggerConfig load(File file) {
        return FileLoggerConfig.builder()
                .setPath("")
                .setFormat("")
                .build();
    }
    public static LoggerConfig load(InputStream in) {
        return FileLoggerConfig.builder()
                .setPath("")
                .setFormat("")
                .build();
    }
}
