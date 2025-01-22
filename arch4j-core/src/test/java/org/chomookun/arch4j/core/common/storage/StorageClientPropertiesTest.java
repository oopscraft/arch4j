package org.chomookun.arch4j.core.common.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

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