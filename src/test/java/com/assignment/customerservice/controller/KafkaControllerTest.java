package com.assignment.customerservice.controller;

import com.assignment.customerservice.dto.CustomerBatchDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KafkaController.class)
public class KafkaControllerTest {

    @MockBean
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @InjectMocks
    private KafkaController kafkaController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSendMessage() throws Exception {
        CustomerBatchDto message = new CustomerBatchDto();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/messages/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully!"));
    }
}
