package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.dao.MessageEntity;
import org.oopscraft.arch4j.core.message.dao.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message saveMessage(Message message) {
        MessageEntity messageEntity = messageRepository.findById(message.getMessageId())
                .orElse(MessageEntity.builder()
                    .messageId(message.getMessageId())
                    .build());

        messageEntity.setMessageName(message.getMessageName());
        messageEntity.setValue(message.getValue());
        messageEntity.setNote(message.getNote());

        messageEntity = messageRepository.saveAndFlush(messageEntity);

        return Message.from(messageEntity);
    }

    public Optional<Message> getMessage(String messageId) {
        return messageRepository.findById(messageId).map(Message::from);
    }

    @Transactional
    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
        messageRepository.flush();
    }

    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {
        Page<MessageEntity> messageEntityPage = messageRepository.findAll(messageSearch, pageable);
        List<Message> messages = messageEntityPage.getContent().stream()
                .map(Message::from)
                .collect(Collectors.toList());
        return new PageImpl<>(messages, pageable, messageEntityPage.getTotalElements());
    }

}
