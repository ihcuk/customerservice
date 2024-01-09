package com.assignment.customerservice.utils;

import org.springframework.util.ObjectUtils;

public interface CustomerServiceUtility {

    public static boolean isEmpty(Object input) {
        return ObjectUtils.isEmpty(input);
    }
}
