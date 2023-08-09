package org.oopscraft.arch4j.core.message.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_message_language")
@IdClass(MessageLanguageEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageLanguageEntity extends SystemFieldEntity implements LanguageEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String messageId;
        private String language;
    }

    @Id
    @Column(name = "message_id", length = 64)
    private String messageId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "value")
    private String value;

}

