package org.chomoo.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomoo.arch4j.web.WebProperties;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.web.common.test.WebTestSupport;

@RequiredArgsConstructor
@Slf4j
class WebPropertiesTest extends WebTestSupport {

    private final WebProperties webProperties;

    @Test
    public void testLoad() {
        log.debug("== webProperties:{}", webProperties);
    }

}