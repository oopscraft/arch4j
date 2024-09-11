package org.oopscraft.arch4j.cli.jasypt;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.cli.common.CliTestSupport;

@RequiredArgsConstructor
class JasyptCommandTest extends CliTestSupport {

    private final JasyptCommand pbeCommand;

    @Test
    public void encrypt() {
        pbeCommand.encrypt("test");
    }

}