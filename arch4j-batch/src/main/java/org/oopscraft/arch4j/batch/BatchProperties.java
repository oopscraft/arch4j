package org.oopscraft.arch4j.batch;

import com.zaxxer.hikari.HikariConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "batch")
@ConstructorBinding
@AllArgsConstructor
@Getter
public final class BatchProperties {

    private final HikariConfig datasource;

}
