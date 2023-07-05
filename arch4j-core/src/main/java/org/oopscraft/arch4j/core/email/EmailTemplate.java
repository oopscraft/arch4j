package org.oopscraft.arch4j.core.email;

import lombok.*;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateEntity;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {

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
                .templateId(emailTemplateEntity.getTemplateId())
                .templateName(emailTemplateEntity.getTemplateName())
                .subject(emailTemplateEntity.getSubject())
                .content(emailTemplateEntity.getContent())
                .build();
    }

}
