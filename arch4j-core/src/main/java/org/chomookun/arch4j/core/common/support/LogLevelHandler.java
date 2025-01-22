package org.chomookun.arch4j.core.common.support;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class LogLevelHandler implements Closeable {

    @Data
    public static class Preset {
        final String loggerName;
        final String level;
    }

    /**
     * ROOT preset
     */
    public static final List<Preset> ROOT = new ArrayList<>(){{
        add(new Preset("root", "DEBUG"));
    }};

    /**
     * JPA preset
     */
    public static final List<Preset> JPA = new ArrayList<>(){{
        add(new Preset("org.hibernate.SQL", "DEBUG"));
        add(new Preset("org.hibernate.type.descriptor.jdbc.JdbcTypeJavaDescriptor", "TRACE"));
        add(new Preset("org.hibernate.type.descriptor.jdbc.JdbcTypeDescriptor", "TRACE"));
    }};

    Map<Logger, Level> originLogLevels = new LinkedHashMap<>();

    /**
     * set log level
     * @param loggerName logger name
     * @param level log level
     */
    public void setLevel(String loggerName, String level) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        Level originLevel = logger.getEffectiveLevel();
        originLogLevels.put(logger, originLevel);
        logger.setLevel(Level.toLevel(level));
    }

    /**
     * set log level
     * @param presets log level preset
     */
    public void setLevel(List<Preset> presets) {
        for(Preset preset : presets) {
            setLevel(preset.getLoggerName(), preset.getLevel());
        }
    }

    /**
     * close
     */
    @Override
    public void close() {
        for(Logger logger : originLogLevels.keySet()){
            try {
                Level originLevel = originLogLevels.get(logger);
                logger.setLevel(originLevel);
            }catch(Throwable t){
                log.warn(t.getMessage());
            }
        }
    }

}

