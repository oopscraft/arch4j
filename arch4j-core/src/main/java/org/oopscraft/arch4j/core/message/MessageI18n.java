package org.oopscraft.arch4j.core.message;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.message.repository.MessageI18nEntity;

@Data
@Builder
public class MessageI18n {

    private String messageId;

    private String language;

    private String value;

    /**
     * factory method
     * @param messageI18nEntity messageI18Entity
     * @return messageI18nEntity
     */
    public static MessageI18n from(MessageI18nEntity messageI18nEntity) {
        return MessageI18n.builder()
                .messageId(messageI18nEntity.getMessageId())
                .language(messageI18nEntity.getLanguage())
                .value(messageI18nEntity.getValue())
                .build();
    }

}
