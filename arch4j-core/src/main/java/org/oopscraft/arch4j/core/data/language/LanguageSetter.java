package org.oopscraft.arch4j.core.data.language;

import org.springframework.util.Assert;

import java.util.function.Consumer;

public class LanguageSetter<T extends LanguageEntity> extends LanguageFunction<T> {

    private final Object currentValue;

    private Runnable defaultSetter;

    private Consumer<T> languageSetter;

    public static <T extends LanguageEntity> LanguageSetter<T> of(LanguageSupportEntity<T> target, Object currentValue) {
        return new LanguageSetter<>(target, currentValue);
    }

    public LanguageSetter(LanguageSupportEntity<T> target, Object currentValue) {
        super(target);
        this.currentValue = currentValue;
    }

    public LanguageSetter<T> defaultSet(Runnable defaultSetter) {
        this.defaultSetter = defaultSetter;
        return this;
    }

    public LanguageSetter<T> languageSet(Consumer<T> languageSetter) {
        this.languageSetter = languageSetter;
        return this;
    }

    public void set() {
        Assert.notNull(defaultSetter, "defaultSetter is null");
        Assert.notNull(languageSetter, "languageSetter is null");
        if(isDefaultLanguage()) {
            defaultSetter.run();
        }else{
            languageSetter.accept(this.createAndGetCurrentLanguageEntity());
        }
        // fallback
        if(this.currentValue == null) {
            defaultSetter.run();
        }
    }

}
