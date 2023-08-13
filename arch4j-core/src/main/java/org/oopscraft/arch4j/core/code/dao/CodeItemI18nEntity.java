package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.i18n.I18nEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_code_item_i18n")
@IdClass(CodeItemI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemI18nEntity extends SystemFieldEntity implements I18nEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String codeId;
        private String itemId;
        private String language;
    }

    @Id
    @Column(name = "code_id", length = 32)
    private String codeId;

    @Id
    @Column(name = "item_id", length = 32)
    private String itemId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "item_name")
    private String itemName;

}

