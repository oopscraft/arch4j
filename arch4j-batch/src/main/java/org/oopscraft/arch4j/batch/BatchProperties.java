package org.oopscraft.arch4j.batch;

import com.zaxxer.hikari.HikariConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch")
@AllArgsConstructor
@Getter
public final class BatchProperties {

    private final HikariConfig datasource;

}
