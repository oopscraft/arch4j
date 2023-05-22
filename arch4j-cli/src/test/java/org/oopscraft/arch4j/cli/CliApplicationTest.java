package org.oopscraft.arch4j.cli;

import org.junit.jupiter.api.Test;

public class CliApplicationTest {

    @Test
    public void generateSchema() {
        CliApplication.main(new String[]{
                "database",
                "generate-schema",
                "test"
        });
    }
}
