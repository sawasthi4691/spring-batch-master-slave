package com.learn.batch.controller;

import com.learn.batch.service.JobLauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class TestController {



    @Autowired
    private JobLauncherService jobLauncherService;


    @GetMapping("/test")
    public ResponseEntity<String> testMethod() {
        return new ResponseEntity<>("Testing show it is up!!!!", HttpStatusCode.valueOf(200));
    }


    @GetMapping("/start")
    public ResponseEntity<String> startJob() {
        jobLauncherService.runMasterJob(); // Trigger the job
        return ResponseEntity.ok("Job started successfully.");
    }
}
