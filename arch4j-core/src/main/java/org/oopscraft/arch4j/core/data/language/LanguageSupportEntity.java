package org.oopscraft.arch4j.core.data.language;

import java.util.List;

public interface LanguageSupportEntity<T extends LanguageEntity> {

    List<T> provideLanguageEntities();

    T provideNewLanguageEntity(String language);

}


