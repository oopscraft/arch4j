package org.oopscraft.arch4j.core.page;

import java.util.Properties;

public abstract class PageWidgetSupport {

    public abstract PageWidgetDefinition getDefinition();

    public abstract String getUrl(Properties properties) ;

}
