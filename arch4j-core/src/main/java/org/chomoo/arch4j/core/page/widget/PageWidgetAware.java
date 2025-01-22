package org.chomoo.arch4j.core.page.widget;

import java.util.Properties;

public abstract class PageWidgetAware {

    public abstract PageWidgetDefinition getDefinition();

    public abstract String getUrl(Properties properties) ;

}
