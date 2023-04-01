package org.oopscraft.arch4j.web.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.oopscraft.arch4j.core.CoreApplication;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.web.WebApplication;
import org.oopscraft.arch4j.web.test.ControllerTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class SampleControllerTest extends ControllerTestSupport {

    Sample testSample = Sample.builder()
            .id("test id")
            .name("test name")
            .type(SampleType.A)
            .build();

    /**
     * test save sample
     * @throws Exception exception
     */
    @Test
    public void saveSample() throws Exception {
        this.mockMvc.perform(post("/sample/save-sample")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSample))
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * test get sample
     * @throws Exception exception
     */
    @Test
    public void getSample() throws Exception {

        // save test sample
        this.saveSample();

        // test get sample
        this.mockMvc.perform(get("/sample/get-sample").param("id", testSample.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testSample.getId()));
    }

    /**
     * test delete sample
     * @throws Exception exception
     */
    @Test
    public void deleteSample() throws Exception {

        // save test data
        saveSample();

        // test delete sample
        this.mockMvc.perform(get("/sample/delete-sample").param("id", testSample.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * test search samples
     * @throws Exception exception
     */
    @Test
    public void getSamples() throws Exception {
        this.mockMvc.perform(get("/sample/get-samples"))
                .andDo(print())
                .andExpect(status().isOk());
    }


}