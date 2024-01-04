package org.oopscraft.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;
import org.oopscraft.arch4j.core.data.i18n.I18nGetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSupportEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "core_email_template")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateEntity extends BaseEntity implements I18nSupportEntity<EmailTemplateI18nEntity> {

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Column(name = "template_name", length = 128)
    private String templateName;

    @Column(name = "subject", length = 1024)
    private String subject;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "template_id", updatable = false)
    @Builder.Default
    private List<EmailTemplateI18nEntity> emailTemplateI18ns = new ArrayList<>();

    @Override
    public List<EmailTemplateI18nEntity> provideI18nEntities() {
        return this.emailTemplateI18ns;
    }

    @Override
    public EmailTemplateI18nEntity provideNewI18nEntity(String language) {
        return EmailTemplateI18nEntity.builder()
                .templateId(this.templateId)
                .language(language)
                .build();
    }

    public void setSubject(String subject) {
        I18nSetter.of(this, this.subject)
                .whenDefault(() -> this.subject = subject)
                .whenI18n(emailTemplateLanguageEntity -> emailTemplateLanguageEntity.setSubject(subject))
                .set();
    }

    public String getSubject() {
        return I18nGetter.of(this, this.subject)
                .whenDefault(() -> this.subject)
                .whenI18n(EmailTemplateI18nEntity::getSubject)
                .get();
    }

    public void setContent(String content) {
        I18nSetter.of(this, this.content)
                .whenDefault(() -> this.content = content)
                .whenI18n(emailTemplateLanguageEntity -> emailTemplateLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return I18nGetter.of(this, this.content)
                .whenDefault(() -> this.content)
                .whenI18n(EmailTemplateI18nEntity::getContent)
                .get();
    }

}
