package org.chomookun.arch4j.core.common.i18n;

import java.util.List;

public interface I18nSupportEntity<T extends I18nEntity> {

    List<T> provideI18nEntities();

    T provideNewI18nEntity(String language);

}


