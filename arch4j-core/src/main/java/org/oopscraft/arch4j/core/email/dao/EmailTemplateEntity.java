package org.oopscraft.arch4j.core.email.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "core_email_template")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateEntity extends SystemFieldEntity {

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

}
