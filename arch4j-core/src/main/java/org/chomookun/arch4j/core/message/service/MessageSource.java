package org.chomookun.arch4j.core.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.message.model.Message;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
public class MessageSource extends ReloadableResourceBundleMessageSource {

    private static final String PROPERTIES_SUFFIX = ".properties";

    private final MessageService messageService;

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    @NonNull
    protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
        if (filename.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
            return refreshClassPathProperties(filename, propHolder);
        } else {
            return super.refreshProperties(filename, propHolder);
        }
    }

    private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
        Properties properties = new Properties();
        long lastModified = -1;
        try {
            Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
            for (Resource resource : resources) {
                String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
                PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
                properties.putAll(holder.getProperties());
                if (lastModified < resource.lastModified())
                    lastModified = resource.lastModified();
            }
        } catch (IOException ignored) {
        }
        return new PropertiesHolder(properties, lastModified);
    }

    @Override
    protected String resolveCodeWithoutArguments(@NonNull String code, @NonNull Locale locale) {
        String result = super.resolveCodeWithoutArguments(code, locale);
        if(result == null) {
            Message message = messageService.getMessage(code).orElse(null);
            if(message != null) {
                result = message.getValue();
            }
        }
        return result;
    }

    @Override
    protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {
        MessageFormat messageFormat = super.resolveCode(code, locale);
        if(messageFormat == null) {
           Message message = messageService.getMessage(code).orElse(null);
            if(message != null) {
                messageFormat = new MessageFormat(message.getValue(), locale);
            }
        }
        return messageFormat;
    }

}
