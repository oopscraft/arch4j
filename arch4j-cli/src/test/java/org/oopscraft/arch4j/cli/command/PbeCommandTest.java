package org.oopscraft.arch4j.cli.command;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.cli.test.CliTestSupport;

@RequiredArgsConstructor
class PbeCommandTest extends CliTestSupport {

    private final PbeCommand pbeCommand;

    @Test
    public void encrypt() {
        pbeCommand.encrypt("test");
    }

}