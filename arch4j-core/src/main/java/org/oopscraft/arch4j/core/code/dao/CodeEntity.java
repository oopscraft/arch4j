package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemEntity;
import org.oopscraft.arch4j.core.data.i18n.I18nGetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSupportEntity;

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
public class CodeEntity extends SystemEntity implements I18nSupportEntity<CodeI18nEntity> {
	
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
    private List<CodeI18nEntity> i18ns = new ArrayList<>();

    @Override
    public List<CodeI18nEntity> provideI18nEntities() {
        return this.i18ns;
    }

    @Override
    public CodeI18nEntity provideNewI18nEntity(String language) {
        return CodeI18nEntity.builder()
                .codeId(this.codeId)
                .language(language)
                .build();
    }

    public void setCodeName(String codeName) {
        I18nSetter.of(this, this.codeName)
                .whenDefault(() -> this.codeName = codeName)
                .whenI18n(codeLanguageEntity -> codeLanguageEntity.setCodeName(codeName))
                .set();
    }

    public String getCodeName() {
        return I18nGetter.of(this, this.codeName)
                .whenDefault(() -> this.codeName)
                .whenI18n(CodeI18nEntity::getCodeName)
                .get();
    }

}
