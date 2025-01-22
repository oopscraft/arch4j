package org.chomoo.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.i18n.I18nGetter;
import org.chomoo.arch4j.core.common.i18n.I18nSetter;
import org.chomoo.arch4j.core.common.i18n.I18nSupportEntity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "core_email")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailEntity extends BaseEntity implements I18nSupportEntity<EmailI18nEntity> {

    @Id
    @Column(name = "email_id", length = 32)
    private String emailId;

    @Column(name = "name")
    private String name;

    @Column(name = "subject", length = 1024)
    private String subject;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "template_id", updatable = false)
    @Builder.Default
    private List<EmailI18nEntity> emailI18ns = new ArrayList<>();

    @Override
    public List<EmailI18nEntity> provideI18nEntities() {
        return this.emailI18ns;
    }

    @Override
    public EmailI18nEntity provideNewI18nEntity(String language) {
        return EmailI18nEntity.builder()
                .emailId(this.emailId)
                .language(language)
                .build();
    }

    public void setSubject(String subject) {
        I18nSetter.of(this, this.subject)
                .whenDefault(() -> this.subject = subject)
                .whenI18n(emailLanguageEntity -> emailLanguageEntity.setSubject(subject))
                .set();
    }

    public String getSubject() {
        return I18nGetter.of(this, this.subject)
                .whenDefault(() -> this.subject)
                .whenI18n(EmailI18nEntity::getSubject)
                .get();
    }

    public void setContent(String content) {
        I18nSetter.of(this, this.content)
                .whenDefault(() -> this.content = content)
                .whenI18n(emailLanguageEntity -> emailLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return I18nGetter.of(this, this.content)
                .whenDefault(() -> this.content)
                .whenI18n(EmailI18nEntity::getContent)
                .get();
    }

}
