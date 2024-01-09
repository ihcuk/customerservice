package com.assignment.customerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock
    private Paths paths;

    @Mock
    private Files files;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadFileToByteArray() throws IOException {
        String filePath = "C:/Users/Shashank Jaiswal/Desktop/CreateShoppingPostHeaders.PNG";
        byte[] actualData = fileService.readFileToByteArray(filePath);
        verify(files, times(1));
        Files.readAllBytes(Paths.get(filePath));
        assertNotNull( actualData);
    }

}
