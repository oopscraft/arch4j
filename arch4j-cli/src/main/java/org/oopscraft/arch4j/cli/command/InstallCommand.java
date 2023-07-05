package org.oopscraft.arch4j.cli.command;

import org.oopscraft.arch4j.cli.CliApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "install")
@Component
public class InstallCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
