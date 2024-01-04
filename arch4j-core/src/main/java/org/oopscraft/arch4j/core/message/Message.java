package org.oopscraft.arch4j.core.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;
import org.oopscraft.arch4j.core.message.dao.MessageEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseEntity {

    private String messageId;

    private String messageName;

    private String value;

    private String note;

    public static Message from(MessageEntity messageEntity) {
        return Message.builder()
                .messageId(messageEntity.getMessageId())
                .messageName(messageEntity.getMessageName())
                .value(messageEntity.getValue())
                .note(messageEntity.getNote())
                .build();
    }

}
