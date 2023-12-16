package org.oopscraft.arch4j.web.support;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WebsocketLogAppender extends AppenderBase<ILoggingEvent> {

    private final SimpMessagingTemplate messagingTemplate;

    private final String destination;

    private final PatternLayout layout;

    public WebsocketLogAppender(SimpMessagingTemplate messagingTemplate, String destination, Context context) {
        this.messagingTemplate = messagingTemplate;
        this.destination = destination;
        layout = new PatternLayout();
        layout.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] - %msg");
        layout.setContext(context);
        layout.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        String logMessage = layout.doLayout(event);
        messagingTemplate.convertAndSend(destination, logMessage);
    }

}
