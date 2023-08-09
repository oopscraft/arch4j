package org.oopscraft.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_email_template_language")
@IdClass(EmailTemplateLanguageEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateLanguageEntity extends SystemFieldEntity implements LanguageEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String templateId;
        private String language;
    }

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    @Lob
    private String content;

}

