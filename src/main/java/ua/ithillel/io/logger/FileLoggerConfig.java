package ua.ithillel.io.logger;

public class FileLoggerConfig implements LoggerConfig {
    private String path;
    private String format;

//    public FileLoggerConfig(String path, String format) {
//        this.path = path;
//        this.format = format;
//    }

    public static class FileConfigBuilder {
        private FileLoggerConfig config = new FileLoggerConfig();


        public FileConfigBuilder setPath(String path) {
            this.config.path = path;
            return this;
        }

        public FileConfigBuilder setFormat(String format) {
            this.config.format = format;
            return this;
        }

        public FileLoggerConfig build() {
            return config;
        }
    }



    public static FileConfigBuilder builder() {
        return new FileConfigBuilder();
    }

    public String getPath() {
        return path;
    }

    public String getFormat() {
        return format;
    }
}
