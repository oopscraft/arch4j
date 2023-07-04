package org.oopscraft.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class EmailServiceTest extends CoreTestSupport {

    private final EmailService emailService;

    @Test
    public void sendMessage() {
        emailService.sendMail("oopscraft.org@gmail.com", "subject", "content");
    }

}