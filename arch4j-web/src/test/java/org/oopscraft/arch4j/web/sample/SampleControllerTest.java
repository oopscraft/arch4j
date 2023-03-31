package org.oopscraft.arch4j.web.sample;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.oopscraft.arch4j.core.CoreApplication;
import org.oopscraft.arch4j.web.WebApplication;
import org.oopscraft.arch4j.web.test.ControllerTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SampleControllerTest extends ControllerTestSupport {

    @Test
    @WithMockUser(username = "test", roles = {"TEST"})
    public void getSamples() throws Exception {
        this.mockMvc.perform(get("/sample/get-samples"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}