package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEntity extends SystemFieldEntity implements LanguageSupportEntity<CodeLanguageEntity> {
	
	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Column(name = "code_name", length = 128)
	private String codeName;
	
	@Column(name = "note")
	@Lob
	private String note;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "code_id", updatable = false)
	@OrderBy(CodeItemEntity_.SORT)
	@Builder.Default
    @Setter(AccessLevel.NONE)
	private List<CodeItemEntity> codeItemEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "code_id", updatable = false)
    @Builder.Default
    private List<CodeLanguageEntity> codeLanguageEntities = new ArrayList<>();

    @Override
    public List<CodeLanguageEntity> provideLanguageEntities() {
        return this.codeLanguageEntities;
    }

    @Override
    public CodeLanguageEntity provideNewLanguageEntity(String language) {
        return CodeLanguageEntity.builder()
                .codeId(this.codeId)
                .language(language)
                .build();
    }

    public void setCodeName(String codeName) {
        LanguageSetter.of(this, this.codeName)
                .defaultSet(() -> this.codeName = codeName)
                .languageSet(codeLanguageEntity -> codeLanguageEntity.setCodeName(codeName))
                .set();
    }

    public String getCodeName() {
        return LanguageGetter.of(this, this.codeName)
                .defaultGet(() -> this.codeName)
                .languageGet(CodeLanguageEntity::getCodeName)
                .get();
    }

}
