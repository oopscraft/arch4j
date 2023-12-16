package org.oopscraft.arch4j.web.websoket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/app/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        // /app/hello로 메시지를 보내면 /topic/greetings로 응답
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 3_000)
    public void greeting2() {
        Greeting greeting = new Greeting("@@@@@@@@@@greeting2@@@@@@@@@@@@@");
        messagingTemplate.convertAndSend("/topic/greetings2", greeting);
    }

}
