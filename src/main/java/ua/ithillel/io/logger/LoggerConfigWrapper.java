package ua.ithillel.io.logger;

public class LoggerConfigWrapper implements LoggerConfig {
    private LoggerConfig loggerConfig;

    @Override
    public String getPath() {
        return loggerConfig.getPath();
    }

    @Override
    public String getFormat() {
        return getFormat() + "%s";
    }
}
