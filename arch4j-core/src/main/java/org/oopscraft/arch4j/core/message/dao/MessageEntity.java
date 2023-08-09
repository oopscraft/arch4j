package org.oopscraft.arch4j.core.message.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;

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
public class MessageEntity extends SystemFieldEntity implements LanguageSupportEntity<MessageLanguageEntity> {

    @Id
    @Column(name = "message_id", length = 64)
    private String messageId;

    @Column(name = "message_name")
    private String messageName;

    @Column(name = "value")
    @Lob
    private String value;

    @Column(name = "note")
    @Lob
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", updatable = false)
    @Builder.Default
    private List<MessageLanguageEntity> messageLanguageEntities = new ArrayList<>();

    @Override
    public List<MessageLanguageEntity> provideLanguageEntities() {
        return this.messageLanguageEntities;
    }

    @Override
    public MessageLanguageEntity provideNewLanguageEntity(String language) {
        return MessageLanguageEntity.builder()
                .messageId(this.messageId)
                .language(language)
                .build();
    }

    public void setValue(String value) {
        LanguageSetter.of(this, this.value)
                .defaultSet(() -> this.value = value)
                .languageSet(messageLanguageEntity -> messageLanguageEntity.setValue(value))
                .set();
    }

    public String getValue() {
        return LanguageGetter.of(this, this.value)
                .defaultGet(() -> this.value)
                .languageGet(MessageLanguageEntity::getValue)
                .get();
    }

}
