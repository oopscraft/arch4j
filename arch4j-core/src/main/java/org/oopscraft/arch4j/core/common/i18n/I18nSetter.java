package org.oopscraft.arch4j.core.common.i18n;

import org.springframework.util.Assert;

import java.util.function.Consumer;

public class I18nSetter<T extends I18nEntity> extends I18nFunction<T> {

    private final Object currentValue;

    private Runnable defaultSetter;

    private Consumer<T> languageSetter;

    public static <T extends I18nEntity> I18nSetter<T> of(I18nSupportEntity<T> target, Object currentValue) {
        return new I18nSetter<>(target, currentValue);
    }

    public I18nSetter(I18nSupportEntity<T> target, Object currentValue) {
        super(target);
        this.currentValue = currentValue;
    }

    public I18nSetter<T> whenDefault(Runnable defaultSetter) {
        this.defaultSetter = defaultSetter;
        return this;
    }

    public I18nSetter<T> whenI18n(Consumer<T> languageSetter) {
        this.languageSetter = languageSetter;
        return this;
    }

    public void set() {
        Assert.notNull(defaultSetter, "defaultSetter is null");
        Assert.notNull(languageSetter, "languageSetter is null");
        if(isDefaultLanguage()) {
            defaultSetter.run();
        }else{
            languageSetter.accept(this.createAndGetCurrentI18nEntity());
        }
        // fallback
        if(this.currentValue == null) {
            defaultSetter.run();
        }
    }

}
