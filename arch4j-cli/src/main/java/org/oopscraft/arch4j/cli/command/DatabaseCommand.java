package org.oopscraft.arch4j.cli.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picocli.CommandLine;

@CommandLine.Command(name = "database")
@Component
public class DatabaseCommand {

    @CommandLine.Command(name = "generate-schema")
    @Transactional
    public Integer generateSchema(@CommandLine.Parameters(index = "0") String entity) {
        System.out.println("######################" + entity);
        return 0;
    }

}
