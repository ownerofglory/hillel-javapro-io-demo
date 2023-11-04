package ua.ithillel.io.logger;

public interface LoggerConfig {
    static String DEFAULT_FORMAT = "[%s]:[%s]";

    public default String getPath() {
        return "";
    }
    public default String getFormat() {
        return DEFAULT_FORMAT;
    }

}
