package org.oopscraft.arch4j.core.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.message.dao.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends SystemFieldEntity {

    private String messageId;

    private String messageName;

    private String value;

    private String note;

    @Builder.Default
    private List<MessageI18n> i18ns = new ArrayList<>();

    public String getValue(Locale locale) {
        return i18ns.stream()
                .filter(el -> el.getLanguage().equals(locale.getLanguage()))
                .findFirst()
                .map(MessageI18n::getValue)
                .orElse(this.value);
    }

    public static Message from(MessageEntity messageEntity) {
        return Message.builder()
                .messageId(messageEntity.getMessageId())
                .messageName(messageEntity.getMessageName())
                .value(messageEntity.getValue())
                .note(messageEntity.getNote())
                .i18ns(messageEntity.getI18ns().stream()
                        .map(MessageI18n::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
