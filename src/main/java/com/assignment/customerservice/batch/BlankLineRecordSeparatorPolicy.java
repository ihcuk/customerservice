package com.assignment.customerservice.batch;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {
    @Override
    public boolean isEndOfRecord(final String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(final String record) {
        if (ObjectUtils.isEmpty(record)) {
            return null;
        }
        return super.postProcess(record);
    }
}
