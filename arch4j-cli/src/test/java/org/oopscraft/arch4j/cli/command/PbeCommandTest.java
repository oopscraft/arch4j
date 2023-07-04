package org.oopscraft.arch4j.cli.command;

import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.cli.CliApplication;

class PbeCommandTest {

    @Test
    public void encrypt() {
        CliApplication.main(new String[]{
                "pbe",
                "encrypt",
                "test_value"
        });
    }

}