package org.oopscraft.arch4j.cli;

import org.junit.jupiter.api.Test;

public class CliApplicationTest {

    @Test
    public void getSchema() {
        CliApplication.main(new String[]{
                "database",
                "get-schema"
        });
    }
}
