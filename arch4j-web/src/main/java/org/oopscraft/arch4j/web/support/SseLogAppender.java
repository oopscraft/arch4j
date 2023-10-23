package org.oopscraft.arch4j.web.support;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class SseLogAppender extends AppenderBase<ILoggingEvent> {

    private final PatternLayout layout;

    private final List<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    public SseLogAppender(Context context) {
        layout = new PatternLayout();
        layout.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] - %msg");
        layout.setContext(context);
        layout.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            String logMessage = layout.doLayout(event);
            for(String logMessageLine : logMessage.split("\n")) {
                for(SseEmitter sseEmitter : sseEmitters) {
                    sseEmitter.send(logMessageLine);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public synchronized void addSseEmitter(SseEmitter sseEmitter) {
        sseEmitters.add(sseEmitter);
    }

    public synchronized void removeSseEmitter(SseEmitter sseEmitter) {
        sseEmitters.remove(sseEmitter);
    }

}
