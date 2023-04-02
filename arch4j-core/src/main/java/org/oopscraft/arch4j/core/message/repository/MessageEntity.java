package org.oopscraft.arch4j.core.message.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;

@Entity
@Table(name = "apps_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "value")
    @Lob
    private String value;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    @Lob
    private String note;

}
