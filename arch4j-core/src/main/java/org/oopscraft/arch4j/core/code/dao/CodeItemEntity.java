package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nGetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSupportEntity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code_item")
@IdClass(CodeItemEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemEntity extends BaseEntity implements I18nSupportEntity<CodeItemI18nEntity> {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Pk implements Serializable {
		private String codeId;
		private String itemId;
	}

	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Id
	@Column(name = "item_id", length = 32)
	private String itemId;

    @Column(name = "name")
    private String name;

    @Column(name = "sort")
	private Integer sort;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "code_id", updatable = false),
            @JoinColumn(name = "item_id", updatable = false)
    })
    @Builder.Default
    private List<CodeItemI18nEntity> i18ns = new ArrayList<>();

    @Override
    public List<CodeItemI18nEntity> provideI18nEntities() {
        return this.i18ns;
    }

    @Override
    public CodeItemI18nEntity provideNewI18nEntity(String language) {
        return CodeItemI18nEntity.builder()
                .codeId(this.codeId)
                .itemId(this.itemId)
                .language(language)
                .build();
    }

    public void setName(String name) {
        I18nSetter.of(this, this.name)
                .whenDefault(() -> this.name = name)
                .whenI18n(codeItemLanguageEntity -> codeItemLanguageEntity.setName(name))
                .set();
    }

    public String getName() {
        return I18nGetter.of(this, this.name)
                .whenDefault(() -> this.name)
                .whenI18n(CodeItemI18nEntity::getName)
                .get();
    }

}
