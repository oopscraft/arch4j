package org.oopscraft.arch4j.core.message.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends SystemFieldEntity {

    @Id
    @Column(name = "message_id", length = 64)
    private String messageId;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    @Lob
    private String value;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(mappedBy = MessageI18nEntity_.MESSAGE_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MessageI18nEntity> i18ns = new ArrayList<>();


}
