package org.oopscraft.arch4j.core.data.language;

import org.oopscraft.arch4j.core.CoreProperties;
import org.oopscraft.arch4j.core.CorePropertiesHolder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

public class LanguageFunction<T extends LanguageEntity> {

    private final LanguageSupportEntity<T> target;

    public LanguageFunction(LanguageSupportEntity<T> target) {
        this.target = target;
    }

    protected boolean isDefaultLanguage() {
        return LocaleContextHolder.getLocale()
                .equals(CorePropertiesHolder.getInstance().getDefaultLocale());
    }

    protected Optional<T> getCurrentLanguageEntity() {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return target.provideLanguageEntities().stream()
                .filter(languageEntity -> languageEntity.getLanguage().equals(language))
                .findFirst();
    }

    protected T createAndGetCurrentLanguageEntity() {
        String language = LocaleContextHolder.getLocale().getLanguage();
        T languageEntity = getCurrentLanguageEntity().orElse(null);
        if(languageEntity == null) {
            languageEntity = target.provideNewLanguageEntity(language);
            target.provideLanguageEntities().add(languageEntity);
        }
        return languageEntity;
    }

}
