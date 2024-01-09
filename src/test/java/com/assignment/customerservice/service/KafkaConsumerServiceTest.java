package com.assignment.customerservice.service;

import com.assignment.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class KafkaConsumerServiceTest {

    @Mock
    private FileService fileService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListen() throws IOException {
        Mockito.when(fileService.readFileToByteArray(Mockito.anyString())).thenReturn("MockProfilePhoto".getBytes());
        String message = "{\"password\":\"12345\",\"name\":\"viaan\",\"address\":\"Gurgaon\",\"pincode\":\"122004\",\"profilephoto\":\"C:/Users/Shashank%20Jaiswal/Desktop/CreateShoppingPostHeaders.PNG\"}";
        kafkaConsumerService.listen(message);

        verify(customerRepository, times(1)).save(argThat(customer ->
                "viaan".equals(customer.getName()) &&
                        "12345".equals(customer.getPassword()) &&
                        "Gurgaon".equals(customer.getAddress()) &&
                        122004 == customer.getPincode() &&
                        "MockProfilePhoto".equals(new String(customer.getProfilephoto()))
        ));
        verify(fileService, times(1)).readFileToByteArray(any());
    }
}
