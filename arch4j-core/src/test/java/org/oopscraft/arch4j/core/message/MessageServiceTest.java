package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.menu.repository.MenuEntity;
import org.oopscraft.arch4j.core.message.repository.MessageEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MessageServiceTest extends CoreTestSupport {

    final MessageService messageService;

    Message testMessage = Message.builder()
            .messageId("test_menu")
            .name("test_name")
            .build();

    @Test
    @Order(1)
    void saveMessage() {
        Message savedMessage = messageService.saveMessage(testMessage);
        assertNotNull(savedMessage);
        assertNotNull(entityManager.find(MessageEntity.class, testMessage.getMessageId()));
    }

    @Test
    @Order(2)
    void getMessage() {
        Message savedMessage = messageService.saveMessage(testMessage);
        Message message = messageService.getMessage(savedMessage.getMessageId()).orElse(null);
        assertNotNull(message);
        assertEquals(savedMessage.getMessageId(), message.getMessageId());
    }

    @Test
    @Order(3)
    void deleteMessage() {
        Message savedMessage = messageService.saveMessage(testMessage);
        messageService.deleteMessage(testMessage.getMessageId());
        assertNull(entityManager.find(MessageEntity.class, testMessage.getMessageId()));
    }

}