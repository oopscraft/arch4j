package org.oopscraft.arch4j.core.email;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseModel;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate extends BaseModel {

    private String templateId;

    private String templateName;

    private String subject;

    private String content;

    @Builder.Default
    private Map<String,Object> variables = new LinkedHashMap<>();

    public void addVariable(String key, Object value) {
        variables.put(key, value);
    }

    public static EmailTemplate from(EmailTemplateEntity emailTemplateEntity) {
        return EmailTemplate.builder()
                .systemRequired(emailTemplateEntity.isSystemRequired())
                .systemUpdatedAt(emailTemplateEntity.getSystemUpdatedAt())
                .systemUpdatedBy(emailTemplateEntity.getSystemUpdatedBy())
                .templateId(emailTemplateEntity.getTemplateId())
                .templateName(emailTemplateEntity.getTemplateName())
                .subject(emailTemplateEntity.getSubject())
                .content(emailTemplateEntity.getContent())
                .build();
    }

}
