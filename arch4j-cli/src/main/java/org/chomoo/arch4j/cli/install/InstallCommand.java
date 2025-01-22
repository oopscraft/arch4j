package org.oopscraft.arch4j.cli.install;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "install")
@Component
public class InstallCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

}
