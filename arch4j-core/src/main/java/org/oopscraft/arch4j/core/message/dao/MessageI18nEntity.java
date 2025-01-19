package org.oopscraft.arch4j.core.message.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_message_i18n")
@IdClass(MessageI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageI18nEntity extends BaseEntity implements I18nEntity {

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

