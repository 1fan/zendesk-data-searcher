package com.zendesk.datasearcher.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.model.entity.AbstractEntity;
import com.zendesk.datasearcher.util.FieldUtil;

public class InvertedIndex<T extends AbstractEntity> {

    private Map<String, Map<String, List<Integer>>> index = new HashMap<>();

    private List<T> elements;

    public InvertedIndex(List<T> elements) throws InvalidFieldException {
        this.elements = elements;
        for (int i = 0; i < elements.size(); i++) {
            T element = elements.get(i);
            List<Field> fields = FieldUtil.getFieldsOfClass(element.getClass());
            for (Field field : fields) {
                Map<String, List<Integer>> termStat = index.getOrDefault(field.getName(), new HashMap<>());
                Object fieldValue = FieldUtil.readFiledValue(field, element);
                if (fieldValue instanceof String) {
                    updateFieldStatisticsWithValue((String) fieldValue, termStat, i);
                } else if (fieldValue instanceof Boolean) {
                    updateFieldStatisticsWithValue(fieldValue.toString(), termStat, i);
                } else if (fieldValue instanceof List) {
                    //could be tags, domainNames, etc
                    if (((List<?>) fieldValue).isEmpty()) {
                        updateFieldStatisticsWithValue("", termStat, i);
                    }
                    for (String s : (List<String>) fieldValue) {
                        updateFieldStatisticsWithValue(s, termStat, i);
                    }
                } else if (fieldValue == null) {
                    updateFieldStatisticsWithValue("", termStat, i);
                } else {
                    //field type not supported yet
                    throw new InvalidFieldException(String.format("Field %s of class %s is not supported. Currently only String, Integer, Boolean and List are supported.", field.getName(), element.getClass()));
                }
                index.put(field.getName(), termStat);
            }
        }
    }

    private void updateFieldStatisticsWithValue(String fieldValue, Map<String, List<Integer>> fieldStats, int index) {
        List<Integer> valueOccurrences = fieldStats.getOrDefault(fieldValue, new ArrayList<>());
        valueOccurrences.add(index);
        fieldStats.put(fieldValue, valueOccurrences);
    }

    public List<T> lookUp(String searchTerm, String searchValue) {
        List<T> lookUpResult = new ArrayList<>();

        if (index.get(searchTerm) == null || index.get(searchTerm).size() == 0) {
            return lookUpResult;
        } else {
            List<Integer> searchValueOccurance = index.get(searchTerm).get(searchValue);
            if (searchValueOccurance != null && searchValueOccurance.size() != 0) {
                for (Integer i : searchValueOccurance) {
                    lookUpResult.add(elements.get(i));
                }
            }
            return lookUpResult;
        }
    }
}
