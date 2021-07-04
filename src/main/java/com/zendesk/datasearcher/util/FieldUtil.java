package com.zendesk.datasearcher.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Service;

@Service
public class FieldUtil {

    public List<Field> getFieldsOfClass(Class cls) {
        List<Field> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(field);
        }
        return fields;
    }

    public List<String> getFieldNamesInStringOfClass(Class cls) {
        List<String> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(convertCamelToSnakeNamingConvention(field.getName()));
        }
        return fields;
    }

    public Object readFiledValue(Field field, Object obj) throws InvalidFieldException {
        try {
            return FieldUtils.readField(field, obj, true);
        } catch (Exception e) {
            throw new InvalidFieldException(String.format("Failed to read value of field %s from object %s.", field.getName(), obj.toString()), e);
        }
    }

    public String converSnaketToCamelNamingConvention(String camelText) {
        if (camelText != null) {
            return CaseUtils.toCamelCase(camelText, false, '_');
        } else {
            return null;
        }
    }

    public String convertCamelToSnakeNamingConvention(String camelText) {
        if (camelText == null) {
            return null;
        }
        if ("id".equals(camelText)) {
            return "_id";
        } else {
            return camelText.replaceAll("([A-Z][a-z])", "_$1").toLowerCase();
        }
    }
}
