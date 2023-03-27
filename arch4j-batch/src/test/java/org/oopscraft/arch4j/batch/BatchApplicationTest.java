package org.oopscraft.arch4j.batch;

import org.junit.jupiter.api.Test;

public class BatchApplicationTest {

    @Test
    public void testDefault() {
        BatchApplication.main(new String[]{
                "--spring.batch.job.enabled=true",
                "--spring.batch.job.names=Test"
        });
    }

}
