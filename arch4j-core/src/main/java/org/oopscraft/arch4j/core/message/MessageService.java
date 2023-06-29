package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.dao.MessageEntity;
import org.oopscraft.arch4j.core.message.dao.MessageRepository;
import org.oopscraft.arch4j.core.message.dao.MessageSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * save message
     * @param message message
     * @return message
     */
    public Message saveMessage(Message message) {
        MessageEntity messageEntity = messageRepository.findById(message.getMessageId()).orElse(null);
        if(messageEntity == null) {
            messageEntity = MessageEntity.builder()
                    .messageId(message.getMessageId())
                    .build();
        }
        messageEntity.setName(message.getName());
        messageEntity.setValue(message.getValue());
        messageEntity.setNote(message.getNote());
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        return Message.from(messageEntity);
    }

    /**
     * returns message
     * @param messageId message id
     * @return message
     */
    public Optional<Message> getMessage(String messageId) {
        return messageRepository.findById(messageId).map(Message::from);
    }

    /**
     * deletes message
     * @param messageId message id
     */
    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
        messageRepository.flush();
    }

    /**
     * searches messages
     * @param messageSearch message search condition
     * @param pageable pagination info
     * @return message page
     */
    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {

        // search condition
        Specification<MessageEntity> specification = (root, query, criteriaBuilder) -> null;
        if(messageSearch.getMessageId() != null) {
            specification = specification.and(MessageSpecification.likeMessageId(messageSearch.getMessageId()));
        }
        if(messageSearch.getName() != null) {
            specification = specification.and(MessageSpecification.likeName(messageSearch.getName()));
        }

        // find data
        Page<MessageEntity> messageEntityPage = messageRepository.findAll(specification, pageable);
        List<Message> messages = messageEntityPage.getContent().stream()
                .map(Message::from)
                .collect(Collectors.toList());
        long total = messageEntityPage.getTotalElements();

        // returns
        return new PageImpl<>(messages, pageable, total);
    }

}
