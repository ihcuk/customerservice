package com.assignment.customerservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModelConvertors {

    public String convertObjectToString(Object input) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(input);
    }
}
