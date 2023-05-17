package org.oopscraft.arch4j.core.message.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.menu.repository.MenuI18nEntity;
import org.oopscraft.arch4j.core.menu.repository.MenuI18nEntity_;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apps_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length=64)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    @Lob
    private String value;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(mappedBy = MessageI18nEntity_.ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MessageI18nEntity> i18ns = new ArrayList<>();


}
