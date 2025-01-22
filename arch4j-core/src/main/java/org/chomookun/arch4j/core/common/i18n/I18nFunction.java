package org.chomookun.arch4j.core.common.i18n;

import org.chomookun.arch4j.core.CorePropertiesHolder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;

public class I18nFunction<T extends I18nEntity> {

    private final I18nSupportEntity<T> target;

    public I18nFunction(I18nSupportEntity<T> target) {
        this.target = target;
    }

    protected boolean isDefaultLanguage() {
        return LocaleContextHolder.getLocale()
                .equals(CorePropertiesHolder.getInstance().getDefaultLocale());
    }

    protected Optional<T> getI18nEntity() {
        String language = LocaleContextHolder.getLocale().getLanguage();
        return target.provideI18nEntities().stream()
                .filter(languageEntity -> languageEntity.getLanguage().equals(language))
                .findFirst();
    }

    protected T createAndGetCurrentI18nEntity() {
        String language = LocaleContextHolder.getLocale().getLanguage();
        T languageEntity = getI18nEntity().orElse(null);
        if(languageEntity == null) {
            languageEntity = target.provideNewI18nEntity(language);
            target.provideI18nEntities().add(languageEntity);
        }
        return languageEntity;
    }

}
