package com.assignment.customerservice.controller;

import com.assignment.customerservice.dto.CustomerBatchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String sendMessage(@RequestBody CustomerBatchDto message) throws JsonProcessingException {
        stringKafkaTemplate.send("customers", new ObjectMapper().writeValueAsString(message));
        return "Message sent successfully!";
    }
}
