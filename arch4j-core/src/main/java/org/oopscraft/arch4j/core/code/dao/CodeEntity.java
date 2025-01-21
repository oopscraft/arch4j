package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nGetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSupportEntity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEntity extends BaseEntity implements I18nSupportEntity<CodeI18nEntity> {
	
	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "note")
	@Lob
	private String note;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "code_id", updatable = false)
	@OrderBy(CodeItemEntity_.SORT)
	@Builder.Default
    @Setter(AccessLevel.NONE)
	private List<CodeItemEntity> codeItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "code_id", updatable = false)
    @Builder.Default
    private List<CodeI18nEntity> codeI18ns = new ArrayList<>();

    @Override
    public List<CodeI18nEntity> provideI18nEntities() {
        return this.codeI18ns;
    }

    @Override
    public CodeI18nEntity provideNewI18nEntity(String language) {
        return CodeI18nEntity.builder()
                .codeId(this.codeId)
                .language(language)
                .build();
    }

    public void setName(String name) {
        I18nSetter.of(this, this.name)
                .whenDefault(() -> this.name = name)
                .whenI18n(codeLanguageEntity -> codeLanguageEntity.setName(name))
                .set();
    }

    public String getName() {
        return I18nGetter.of(this, this.name)
                .whenDefault(() -> this.name)
                .whenI18n(CodeI18nEntity::getName)
                .get();
    }

}
