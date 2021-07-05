package com.zendesk.datasearcher.searcher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.model.entity.AbstractEntity;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.FieldUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * This class maintains the inverted index for User, Ticket and Organization datasets, and provides functions to search on the index for a given field and value.
 */
@Component
public class InvertedIndex {
    private Environment env;
    private FieldUtil fieldUtil;
    private JsonFileReader jsonReader;

    private Map<String, Map<String, List<Integer>>> usersIndex = null;
    private Map<String, Map<String, List<Integer>>> ticketsIndex = null;
    private Map<String, Map<String, List<Integer>>> organizationsIndex = null;
    private List<User> users = null;
    private List<Ticket> tickets = null;
    private List<Organization> organizations = null;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setFieldUtil(FieldUtil fieldUtil) {
        this.fieldUtil = fieldUtil;
    }

    @Autowired
    public void setJsonReader(JsonFileReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    /**
     * Search on User index with the given field name and value.
     *
     * @param fieldName  the field name to search on. The field name should be one of the attribute names in {@link User}.
     * @param fieldValue the value to search
     * @return A List of {@link User} that matches the search criteria
     * @throws IOException           when failed to process the JSON file
     * @throws InvalidFieldException when the field type is not supported, or failed to read field value from {@link User} object.
     */
    public List<User> lookUpUser(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        if (this.usersIndex == null) {
            String usersFilePath = env.getProperty("users.filepath", "users.json");
            this.users = jsonReader.parseJson(usersFilePath, User.class);
            this.usersIndex = buildIndex(users);
        }

        return lookUpValueFromIndex(usersIndex, users, fieldName, fieldValue);
    }

    /**
     * Search on Ticket index with the given field name and value.
     *
     * @param fieldName  the field name to search on. The field name should be one of the attribute names in {@link Ticket}.
     * @param fieldValue the value to search
     * @return A List of {@link Ticket} that matches the search criteria
     * @throws IOException           when failed to process the JSON file
     * @throws InvalidFieldException when the field type is not supported, or failed to read field value from {@link Ticket} object.
     */
    public List<Ticket> lookUpTickets(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        if (this.ticketsIndex == null) {
            String ticketsFilePath = env.getProperty("tickets.filepath", "tickets.json");
            this.tickets = jsonReader.parseJson(ticketsFilePath, Ticket.class);
            this.ticketsIndex = buildIndex(tickets);
        }

        return lookUpValueFromIndex(ticketsIndex, tickets, fieldName, fieldValue);
    }

    /**
     * Search on Organization index with the given field name and value.
     *
     * @param fieldName  the field name to search on. The field name should be one of the attribute names in {@link Organization}.
     * @param fieldValue the value to search
     * @return A List of {@link Organization} that matches the search criteria
     * @throws IOException           when failed to process the JSON file
     * @throws InvalidFieldException when the field type is not supported, or failed to read field value from {@link Organization} object.
     */
    public List<Organization> lookUpOrganizations(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        if (this.organizationsIndex == null) {
            String organizationsFilePath = env.getProperty("organizations.filepath", "organizations.json");
            this.organizations = jsonReader.parseJson(organizationsFilePath, Organization.class);
            this.organizationsIndex = buildIndex(organizations);
        }
        return lookUpValueFromIndex(organizationsIndex, organizations, fieldName, fieldValue);
    }

    private <T extends AbstractEntity> List<T> lookUpValueFromIndex(Map<String, Map<String, List<Integer>>> index, List<T> items, String searchTerm, String searchValue) {
        List<T> lookUpResult = new ArrayList<>();

        if (index.get(searchTerm) == null || index.get(searchTerm).size() == 0) {
            return lookUpResult;
        } else {
            List<Integer> searchValueOccurance = index.get(searchTerm).get(searchValue);
            if (searchValueOccurance != null && searchValueOccurance.size() != 0) {
                for (Integer i : searchValueOccurance) {
                    lookUpResult.add(items.get(i));
                }
            }
            return lookUpResult;
        }
    }

    /**
     * Build the inverted index using the given list of dataset.
     *
     * @param elements a List of objects to be processed
     * @param <T>      extends {@link AbstractEntity}
     * @return the built inverted index in the format of {@code Map<String, Map<String, List<Integer>>>}.
     * <ul>
     *     <li>key: field name</li>
     *     <li>
     *         value: a Map contains statistics of all values of this field and occurrences
     *         <ul>
     *             <li>key: field value</li>
     *             <li>value: a List of Integer represents the index that the value occurred in the given list</li>
     *         </ul>
     *     </li>
     *
     * </ul>
     * @throws InvalidFieldException when the field type is not supported, or failed to read the field's value from the object
     */
    private <T extends AbstractEntity> Map<String, Map<String, List<Integer>>> buildIndex(List<T> elements) throws InvalidFieldException {
        Map<String, Map<String, List<Integer>>> index = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            T element = elements.get(i);
            List<Field> fields = fieldUtil.getFieldsOfClass(element.getClass());
            for (Field field : fields) {
                Map<String, List<Integer>> termStat = index.getOrDefault(field.getName(), new HashMap<>());
                Object fieldValue = fieldUtil.readFiledValue(field, element);
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
        return index;
    }

    private void updateFieldStatisticsWithValue(String fieldValue, Map<String, List<Integer>> fieldStats, int index) {
        List<Integer> valueOccurrences = fieldStats.getOrDefault(fieldValue, new ArrayList<>());
        valueOccurrences.add(index);
        fieldStats.put(fieldValue, valueOccurrences);
    }
}
