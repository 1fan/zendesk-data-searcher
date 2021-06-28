package com.zendesk.datasearcher.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Service;

@Service
public class FieldsUtil {

    public List<Field> getFieldsOfClass(Class cls) {
        List<Field> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(field);
        }
        return fields;
    }

    public List<String> getFieldsInStringOfClass(Class cls) {
        List<String> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(field.getName());
        }
        return fields;
    }
}
