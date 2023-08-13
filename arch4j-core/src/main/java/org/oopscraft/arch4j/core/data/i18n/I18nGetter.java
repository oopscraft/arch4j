package org.oopscraft.arch4j.core.data.i18n;

import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.function.Supplier;

public class I18nGetter<T extends I18nEntity,R> extends I18nFunction<T> {

    private final R currentValue;

    private Supplier<R> defaultGetter;

    private Function<T,R> languageGetter;

    public static <T extends I18nEntity,R> I18nGetter<T,R> of(I18nSupportEntity<T> target, R currentValue) {
        return new I18nGetter<>(target, currentValue);
    }

    public I18nGetter(I18nSupportEntity<T> target, R currentValue) {
        super(target);
        this.currentValue = currentValue;
    }

    public I18nGetter<T,R> whenDefault(Supplier<R> defaultGetter) {
        this.defaultGetter = defaultGetter;
        return this;
    }

    public I18nGetter<T,R> whenI18n(Function<T,R> languageGetter) {
        this.languageGetter = languageGetter;
        return this;
    }

    public R get() {
        Assert.notNull(defaultGetter, "defaultGetter is null");
        Assert.notNull(languageGetter, "languageGetter is null");
        R value = null;
        if(this.isDefaultLanguage()) {
            value = this.defaultGetter.get();
        }else{
            T languageEntity = this.getI18nEntity().orElse(null);
            if(languageEntity != null) {
                value = languageGetter.apply(languageEntity);
            }
        }
        // fallback
        if(value == null) {
            value = currentValue;
        }
        return value;
    }

}
