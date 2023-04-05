package org.oopscraft.arch4j.core.message;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.message.repository.MessageEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends SystemFieldEntity {

    private String code;

    private String locale;

    private String message;

    private String name;

    private String note;

    /**
     * factory method
     * @param messageEntity message entity
     * @return message
     */
    public static Message from(MessageEntity messageEntity) {
        return Message.builder()
                .code(messageEntity.getCode())
                .locale(messageEntity.getLocale())
                .message(messageEntity.getMessage())
                .name(messageEntity.getName())
                .note(messageEntity.getNote())
                .build();
    }

}
