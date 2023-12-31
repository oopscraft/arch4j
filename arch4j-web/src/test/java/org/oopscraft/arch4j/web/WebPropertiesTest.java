package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.web.support.WebTestSupport;

@RequiredArgsConstructor
@Slf4j
class WebPropertiesTest extends WebTestSupport {

    private final WebProperties webProperties;

    @Test
    public void testLoad() {
        log.debug("== webProperties:{}", webProperties);
    }

}