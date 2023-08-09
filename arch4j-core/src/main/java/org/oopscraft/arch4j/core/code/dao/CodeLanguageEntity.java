package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_code_language")
@IdClass(CodeLanguageEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeLanguageEntity extends SystemFieldEntity implements LanguageEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String codeId;
        private String language;
    }

    @Id
    @Column(name = "code_id", length = 64)
    private String codeId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "code_name")
    private String codeName;

}

