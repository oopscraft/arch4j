package org.oopscraft.arch4j.core.message.repository;

import jdk.jfr.DataAmount;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(MessageEntity.Pk.class)
@Table(name = "apps_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String code;
        private String locale;
    }

    @Id
    @Column(name = "code", length=64)
    private String code;

    @Id
    @Column(name = "local", length=8)
    private String locale;

    @Column(name = "message")
    @Lob
    private String message;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    @Lob
    private String note;

}
