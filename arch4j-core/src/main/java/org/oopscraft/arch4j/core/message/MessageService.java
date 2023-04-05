package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.repository.MessageEntity;
import org.oopscraft.arch4j.core.message.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * save message
     * @param message message
     */
    public void saveMessage(Message message) {
        MessageEntity.Pk pk = MessageEntity.Pk.builder()
                .code(message.getCode())
                .locale(message.getLocale())
                .build();
        MessageEntity messageEntity = messageRepository.findById(pk).orElse(null);
        if(messageEntity == null) {
            messageEntity = MessageEntity.builder()
                    .code(message.getCode())
                    .locale(message.getLocale())
                    .build();
        }
        messageEntity = messageEntity.toBuilder()
                .message(message.getMessage())
                .name(message.getName())
                .note(message.getNote())
                .build();
        messageRepository.saveAndFlush(messageEntity);
    }

    /**
     * returm message
     * @param id id
     * @return message
     */
    public Optional<Message> getMessage(String code, String locale) {
        MessageEntity.Pk pk = MessageEntity.Pk.builder()
                .code(code)
                .locale(locale)
                .build();
        return messageRepository.findById(pk).map(Message::from);
    }




}
