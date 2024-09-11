package org.oopscraft.arch4j.core.message.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nGetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSupportEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends BaseEntity implements I18nSupportEntity<MessageI18nEntity> {

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", updatable = false)
    @Builder.Default
    private List<MessageI18nEntity> messageI18ns = new ArrayList<>();

    @Override
    public List<MessageI18nEntity> provideI18nEntities() {
        return this.messageI18ns;
    }

    @Override
    public MessageI18nEntity provideNewI18nEntity(String language) {
        return MessageI18nEntity.builder()
                .messageId(this.messageId)
                .language(language)
                .build();
    }

    public void setValue(String value) {
        I18nSetter.of(this, this.value)
                .whenDefault(() -> this.value = value)
                .whenI18n(messageLanguageEntity -> messageLanguageEntity.setValue(value))
                .set();
    }

    public String getValue() {
        return I18nGetter.of(this, this.value)
                .whenDefault(() -> this.value)
                .whenI18n(MessageI18nEntity::getValue)
                .get();
    }

}
