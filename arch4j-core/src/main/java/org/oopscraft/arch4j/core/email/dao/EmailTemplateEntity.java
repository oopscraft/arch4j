package org.oopscraft.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "core_email_template")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateEntity extends SystemFieldEntity implements LanguageSupportEntity<EmailTemplateLanguageEntity> {

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
    private List<EmailTemplateLanguageEntity> emailTemplateLanguageEntities = new ArrayList<>();

    @Override
    public List<EmailTemplateLanguageEntity> provideLanguageEntities() {
        return this.emailTemplateLanguageEntities;
    }

    @Override
    public EmailTemplateLanguageEntity provideNewLanguageEntity(String language) {
        return EmailTemplateLanguageEntity.builder()
                .templateId(this.templateId)
                .language(language)
                .build();
    }

    public void setSubject(String subject) {
        LanguageSetter.of(this, this.subject)
                .defaultSet(() -> this.subject = subject)
                .languageSet(emailTemplateLanguageEntity -> emailTemplateLanguageEntity.setSubject(subject))
                .set();
    }

    public String getSubject() {
        return LanguageGetter.of(this, this.subject)
                .defaultGet(() -> this.subject)
                .languageGet(EmailTemplateLanguageEntity::getSubject)
                .get();
    }

    public void setContent(String content) {
        LanguageSetter.of(this, this.content)
                .defaultSet(() -> this.content = content)
                .languageSet(emailTemplateLanguageEntity -> emailTemplateLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return LanguageGetter.of(this, this.content)
                .defaultGet(() -> this.content)
                .languageGet(EmailTemplateLanguageEntity::getContent)
                .get();
    }

}
