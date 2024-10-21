package org.oopscraft.arch4j.core.common.pbe;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PbePropertiesUtil {

    /**
     * encodes properties string with pbe encryption
     * @param propertiesString properties string
     * @return encoded properties string
     */
    public static String encodePropertiesString(@NotNull String propertiesString) {
        StringBuilder stringBuilder = new StringBuilder();

        try (StringReader stringReader = new StringReader(propertiesString);
             BufferedReader bufferedReader = new BufferedReader(stringReader)) {
            List<String> lines = bufferedReader.lines().toList();
            for(String line : lines) {
                if(line.isBlank() || line.startsWith("#")) {
                    stringBuilder.append(line)
                            .append('\n');
                    continue;
                }
                int indexOf = line.indexOf("=");
                String key = line.substring(0, indexOf);
                String value = line.substring(indexOf+1);
                value = PbeStringUtil.encode(value);
                stringBuilder.append(key)
                        .append("=")
                        .append(value)
                        .append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    /**
     * loads properties from encrypted properties string
     * @param propertiesString properties string
     * @return properties
     */
    public static Properties loadProperties(String propertiesString) {
        Properties properties = new Properties();
        if (propertiesString != null && propertiesString.trim().length() > 0) {
            try {
                properties.load(new StringReader(propertiesString));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        Properties newProperties = new Properties();
        properties.forEach((keyObject, valueObject) -> {
            String key = (String) keyObject;
            String value = Optional.ofNullable(valueObject)
                    .map(Object::toString)
                    .orElse(null);
            if (PbeStringUtil.hasDecryptedMark(value)) {
                value = PbeStringUtil.unwrapDecryptedMark(value);
            }
            if (PbeStringUtil.hasEncryptedMark(value)) {
                value = PbeStringUtil.unwrapEncryptedMark(value);
                value = PbeStringUtil.decrypt(value);
            }
            newProperties.setProperty(key, value);
        });
        return newProperties;
    }

}
