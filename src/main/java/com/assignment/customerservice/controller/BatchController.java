package com.assignment.customerservice.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public BatchController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/runJob")
    public String runJob() {
        try {
            jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
            return "Job started successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error starting the job: " + e.getMessage();
        }
    }
}
