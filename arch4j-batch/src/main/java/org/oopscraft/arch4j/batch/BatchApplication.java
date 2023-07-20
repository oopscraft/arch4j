package org.oopscraft.arch4j.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@Slf4j
public class BatchApplication {

    public static void main(String[] args) {

        // check args
        if(args == null || args.length < 1) {
            String errorMessage = "Usages: BatchClassName name=value ...";
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

        // launch spring boot application
        new SpringApplicationBuilder(BatchConfiguration.class, batchClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

}
