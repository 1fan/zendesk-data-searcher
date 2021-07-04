package com.zendesk.datasearcher.processor;

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

    public List<User> lookUpUser(String term, String value) throws IOException, InvalidFieldException {
        if (this.usersIndex == null) {
            String usersFilePath = env.getProperty("users.filepath", "users.json");
            this.users = jsonReader.parseJson(usersFilePath, User.class);
            this.usersIndex = buildIndex(users);
        }

        return lookUpValueFromIndex(usersIndex, users, term, value);
    }

    public List<Ticket> lookUpTickets(String term, String value) throws IOException, InvalidFieldException {
        if (this.ticketsIndex == null) {
            String ticketsFilePath = env.getProperty("tickets.filepath", "tickets.json");
            this.tickets = jsonReader.parseJson(ticketsFilePath, Ticket.class);
            this.ticketsIndex = buildIndex(tickets);
        }

        return lookUpValueFromIndex(ticketsIndex, tickets, term, value);
    }

    public List<Organization> lookUpOrganizations(String term, String value) throws IOException, InvalidFieldException {
        if (this.organizationsIndex == null) {
            String organizationsFilePath = env.getProperty("organizations.filepath", "organizations.json");
            this.organizations = jsonReader.parseJson(organizationsFilePath, Organization.class);
            this.organizationsIndex = buildIndex(organizations);
        }
        return lookUpValueFromIndex(organizationsIndex, organizations, term, value);
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
        List<Integer> valueOccurance = fieldStats.getOrDefault(fieldValue, new ArrayList<>());
        valueOccurance.add(index);
        fieldStats.put(fieldValue, valueOccurance);
    }
}
