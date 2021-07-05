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

    /**
     * Get all {@link Field} of the given class.
     *
     * @param cls fields to read from
     * @return List of {@link Field} of the given class
     */
    public List<Field> getFieldsOfClass(Class cls) {
        List<Field> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(field);
        }
        return fields;
    }

    /**
     * Get all {@link Field} of the given class and return the field name as a List of String.
     *
     * @param cls fields to read from
     * @return a List of field name in String.
     */
    public List<String> getFieldNamesInStringOfClass(Class cls) {
        List<String> fields = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(cls)) {
            field.setAccessible(true);
            fields.add(convertCamelCaseToSnakeCase(field.getName()));
        }
        return fields;
    }

    /**
     * Read the value of a given field from the object
     *
     * @param field The field to read
     * @param obj   The object to read from
     * @return The value of the given field
     * @throws InvalidFieldException if failed to read field's value from the object
     */
    public Object readFiledValue(Field field, Object obj) throws InvalidFieldException {
        try {
            return FieldUtils.readField(field, obj, true);
        } catch (Exception e) {
            throw new InvalidFieldException(String.format("Failed to read value of field %s from object %s.", field.getName(), obj.toString()), e);
        }
    }

    /**
     * Convert the text from snake case to camel case. For instance, "field_value" to "fieldValue".
     *
     * @param snakeCaseText the text in snake case.
     * @return the converted camel case text. Return null if the input is null.
     */
    public String converSnakeCaseToCamelCase(String snakeCaseText) {
        if (snakeCaseText != null) {
            return CaseUtils.toCamelCase(snakeCaseText, false, '_');
        } else {
            return null;
        }
    }

    /**
     * Convert the text from camel case to snake case. For instance, "fieldValue" to "field_value". Specifically, only "id" will be converted to "_id".
     *
     * @param camelCaseText the text in camel case.
     * @return the converted snake case text.
     */
    public String convertCamelCaseToSnakeCase(String camelCaseText) {
        if (camelCaseText == null) {
            return null;
        }
        if ("id".equals(camelCaseText)) {
            return "_id";
        } else {
            return camelCaseText.replaceAll("([A-Z][a-z])", "_$1").toLowerCase();
        }
    }
}
