package org.oopscraft.arch4j.cli.install;

import org.apache.commons.lang3.ArrayUtils;
import org.oopscraft.arch4j.cli.utils.InteractiveUtils;
import org.oopscraft.arch4j.core.CoreConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "install")
@Component
public class InstallCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
