package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.message.dao.MessageEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MessageServiceTest extends CoreTestSupport {

    final MessageService messageService;

    Message testMessage = Message.builder()
            .messageId("test_menu")
            .messageName("test_name")
            .build();

    @Test
    @Order(1)
    void saveMessage() {
        // when
        Message savedMessage = messageService.saveMessage(testMessage);

        // then
        assertNotNull(savedMessage);
        assertNotNull(entityManager.find(MessageEntity.class, testMessage.getMessageId()));
    }

    @Test
    @Order(2)
    void getMessage() {
        // given
        Message savedMessage = messageService.saveMessage(testMessage);

        // when
        Optional<Message> optionalMessage = messageService.getMessage(savedMessage.getMessageId());

        // then
        assertTrue(optionalMessage.isPresent());
        assertEquals(savedMessage.getMessageId(), optionalMessage.get().getMessageId());
    }

    @Test
    @Order(3)
    void deleteMessage() {
        // given
        Message savedMessage = messageService.saveMessage(testMessage);

        // when
        messageService.deleteMessage(testMessage.getMessageId());

        // then
        assertNull(entityManager.find(MessageEntity.class, testMessage.getMessageId()));
    }

}