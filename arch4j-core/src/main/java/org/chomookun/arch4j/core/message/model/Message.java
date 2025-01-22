package org.chomookun.arch4j.core.message.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.message.dao.MessageEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseModel {

    private String messageId;

    private String name;

    private String value;

    private String note;

    public static Message from(MessageEntity messageEntity) {
        return Message.builder()
                .systemRequired(messageEntity.isSystemRequired())
                .systemUpdatedAt(messageEntity.getSystemUpdatedAt())
                .systemUpdatedBy(messageEntity.getSystemUpdatedBy())
                .messageId(messageEntity.getMessageId())
                .name(messageEntity.getName())
                .value(messageEntity.getValue())
                .note(messageEntity.getNote())
                .build();
    }

}
