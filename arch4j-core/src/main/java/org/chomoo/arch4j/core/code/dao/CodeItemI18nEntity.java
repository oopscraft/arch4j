package org.chomoo.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.i18n.I18nEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_code_item_i18n")
@IdClass(CodeItemI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemI18nEntity extends BaseEntity implements I18nEntity {

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

    @Column(name = "name")
    private String name;

}

