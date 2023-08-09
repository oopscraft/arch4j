package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.code.CodeItem;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;

import javax.persistence.*;
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
public class CodeItemEntity extends SystemFieldEntity implements LanguageSupportEntity<CodeItemLanguageEntity> {

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

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "sort")
	private Integer sort;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "code_id", updatable = false),
            @JoinColumn(name = "item_id", updatable = false)
    })
    @Builder.Default
    private List<CodeItemLanguageEntity> codeItemLanguageEntities = new ArrayList<>();

    @Override
    public List<CodeItemLanguageEntity> provideLanguageEntities() {
        return this.codeItemLanguageEntities;
    }

    @Override
    public CodeItemLanguageEntity provideNewLanguageEntity(String language) {
        return CodeItemLanguageEntity.builder()
                .codeId(this.codeId)
                .itemId(this.itemId)
                .language(language)
                .build();
    }

    public void setItemName(String itemName) {
        LanguageSetter.of(this, this.itemName)
                .defaultSet(() -> this.itemName = itemName)
                .languageSet(codeItemLanguageEntity -> codeItemLanguageEntity.setItemName(itemName))
                .set();
    }

    public String getItemName() {
        return LanguageGetter.of(this, this.itemName)
                .defaultGet(() -> this.itemName)
                .languageGet(CodeItemLanguageEntity::getItemName)
                .get();
    }

}
