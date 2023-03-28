package org.oopscraft.arch4j.batch;

import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.batch.sample.DatabaseToDatabase;

public class BatchApplicationTest {

    @Test
    public void testDefault() {
        String batchClassName = DatabaseToDatabase.class.getName();
        BatchApplication.main(new String[]{
                batchClassName,
                "--spring.batch.job.enabled=true",
                String.format("--spring.batch.job.names=%s", batchClassName)
        });
    }

}
