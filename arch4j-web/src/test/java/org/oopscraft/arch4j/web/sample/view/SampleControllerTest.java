package org.oopscraft.arch4j.web.sample.view;

import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.web.support.WebTestSupport;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class SampleControllerTest extends WebTestSupport {

    Sample testSample = Sample.builder()
            .sampleId("test id")
            .sampleName("test name")
            .sampleType(SampleType.A)
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
        this.mockMvc.perform(get("/sample/get-sample").param("sampleId", testSample.getSampleId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleId").value(testSample.getSampleId()));
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
        this.mockMvc.perform(get("/sample/delete-sample").param("sampleId", testSample.getSampleId()))
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