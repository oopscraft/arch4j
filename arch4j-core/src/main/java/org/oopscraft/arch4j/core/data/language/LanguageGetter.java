package org.oopscraft.arch4j.core.data.language;

import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.function.Supplier;

public class LanguageGetter<T extends LanguageEntity,R> extends LanguageFunction<T> {

    private final R currentValue;

    private Supplier<R> defaultGetter;

    private Function<T,R> languageGetter;

    public static <T extends LanguageEntity,R> LanguageGetter<T,R> of(LanguageSupportEntity<T> target, R currentValue) {
        return new LanguageGetter<>(target, currentValue);
    }

    public LanguageGetter(LanguageSupportEntity<T> target, R currentValue) {
        super(target);
        this.currentValue = currentValue;
    }

    public LanguageGetter<T,R> defaultGet(Supplier<R> defaultGetter) {
        this.defaultGetter = defaultGetter;
        return this;
    }

    public LanguageGetter<T,R> languageGet(Function<T,R> languageGetter) {
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
            T languageEntity = this.getCurrentLanguageEntity().orElse(null);
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
