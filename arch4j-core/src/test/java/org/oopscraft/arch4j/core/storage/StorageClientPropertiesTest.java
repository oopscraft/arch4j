package org.oopscraft.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class StorageClientPropertiesTest extends CoreTestSupport {

    private final StorageClientProperties storageClientProperties;

    @Test
    public void testLoad() {
        assertNotNull(storageClientProperties);
        assertNotNull(storageClientProperties.getBean());
        assertNotNull(storageClientProperties.getProperties());
    }

}