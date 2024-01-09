package com.assignment.customerservice.controller;

import com.assignment.customerservice.controller.BatchController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BatchController.class)
public class BatchControllerTest {

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private Job job;

    @InjectMocks
    private BatchController batchController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRunJobSuccess() throws Exception {
        JobExecution successfulJobExecution = new JobExecution(1L);
        when(jobLauncher.run(eq(job), any(JobParameters.class))).thenReturn(successfulJobExecution);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/batch/runJob"))
                .andExpect(status().isOk())
                .andExpect(content().string("Job started successfully."));
        verify(jobLauncher, times(1)).run(eq(job), any(JobParameters.class));
    }
}
