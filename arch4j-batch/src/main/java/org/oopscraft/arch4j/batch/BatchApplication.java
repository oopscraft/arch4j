package org.oopscraft.arch4j.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class BatchApplication {

    public static void main(String[] args) {

        // check args
        if(args == null || args.length < 2) {
            String errorMessage = "Usages: BatchClassName JobName name=value ...";
            System.err.println(errorMessage);
            log.error(errorMessage);
            System.exit(-1);
        }

        // batch configuration
        Class<?> batchClass;
        try {
            batchClass = Class.forName(args[0]);
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        // properties
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        arguments.add("--spring.batch.job.enabled=true");
        arguments.add("--spring.batch.job.names=" + args[1]);
        args = arguments.toArray(new String[0]);

        // launch spring boot application
        new SpringApplicationBuilder(BatchConfiguration.class, batchClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);

        // exit
        System.exit(0);
    }

}
