package com.zendesk.datasearcher.processor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zendesk.datasearcher.entity.AbstractEntity;
import com.zendesk.datasearcher.entity.Organizations;
import com.zendesk.datasearcher.entity.Tickets;
import com.zendesk.datasearcher.entity.Users;
import com.zendesk.datasearcher.util.FieldsUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Processor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private FieldsUtil fieldsUtil;

    @Autowired
    private JsonFileReader jsonReader;
    private Map<String, Map<String, List<Integer>>> usersIndex = null;
    private Map<String, Map<String, List<Integer>>> ticketsIndex = null;
    private Map<String, Map<String, List<Integer>>> organizationsIndex = null;
    private List<Users> users = null;
    private List<Tickets> tickets = null;
    private List<Organizations> organizations = null;

    public void searchByTickets(String searchTerm, String searchValue) throws IOException {
        List<Tickets> tickets = lookUpTickets(searchTerm, searchValue);
        if (tickets == null || tickets.size() == 0) {
            //not found
            System.out.println("No available data for searched term and value.");
        } else {
            //found
            System.out.println(String.format("Found %d Tickets whose %s is %s:", tickets.size(), searchTerm, "".equals(searchValue) ? "empty" : searchValue));
            for (Tickets ticket : tickets) {
                System.out.println("**************************************");
                System.out.println(ticket);
                System.out.println("---------------");
                System.out.println(String.format("Related Organizations whose _id is : %s", ticket.getOrganizationId()));
                for (Organizations org : lookUpOrganizations("id", ticket.getOrganizationId())) {
                    System.out.println(org.toString());
                }
                System.out.println("---------------");
                System.out.println(String.format("Related Users whose assignee_id is %s: ", ticket.getAssigneeId()));
                for (Users users : lookUpUser("id", ticket.getAssigneeId())) {
                    System.out.println(users.toString());
                }
                System.out.println("---------------");
                System.out.println(String.format("Related Users whose submitter_id is %s: ", ticket.getSubmitterId()));
                for (Users users : lookUpUser("id", ticket.getSubmitterId())) {
                    System.out.println(users.toString());
                }
                System.out.println();
            }
        }
    }

    public void searchByOrganizations(String searchTerm, String searchValue) throws Exception {
        List<Organizations> organizations = lookUpOrganizations(searchTerm, searchValue);
        if (organizations == null || organizations.size() == 0) {
            //not found
            System.out.println("No available data for searched term and value.");
        } else {
            //found
            System.out.println(String.format("Found %d Organizations whose %s is %s:", organizations.size(), searchTerm, "".equals(searchValue) ? "empty" : searchValue));
            for (Organizations org : organizations) {
                System.out.println("**************************************");
                System.out.println(org);
                System.out.println("---------------");
                System.out.println(String.format("Related Users whose organization_id is %s:", org.getId()));
                List<Users> relatedUsers = lookUpUser("organizationId", org.getId());
                for (Users user : relatedUsers) {
                    System.out.println(user.toString());
                }
                System.out.println("---------------");
                List<Tickets> relatedTickets = lookUpTickets("organizationId", org.getId());
                System.out.println(String.format("Related Tickets whose organization_id is %s: ", org.getId()));
                for (Tickets ticket : relatedTickets) {
                    System.out.println(ticket.toString());
                }
                System.out.println();
            }
        }
    }

    /**
     * todo:
     * 1) build the inverse index for the search dataset
     * 2) search the searchValue from the index:
     * if not found, print not found.
     * if found, get the related object;
     * 3) search the related other dataset
     */
    public void searchByUsers(String searchTerm, String searchValue) throws IOException {
        List<Users> users = lookUpUser(searchTerm, searchValue);
        if (users == null || users.size() == 0) {
            //not found
            System.out.println("No available data for searched term and value.");
        } else {
            //found
            System.out.println(String.format("Found %d Users whose %s is %s:", users.size(), searchTerm, "".equals(searchValue) ? "empty" : searchValue));
            for (Users user : users) {
                System.out.println("**************************************");
                System.out.println(user);
                System.out.println("---------------");
                List<Organizations> relatedOrgs = lookUpOrganizations("id", user.getOrganizationId());
                System.out.println(String.format("Related Organizations whose _id is %s:", user.getOrganizationId()));
                for (Organizations org : relatedOrgs) {
                    System.out.println(org.toString());
                }
                System.out.println("---------------");
                System.out.println(String.format("Related Tickets whose submitter_id is %s:", user.getId()));
                for (Tickets relatedTicket : lookUpTickets("submitterId", user.getId())) {
                    System.out.println(relatedTicket.toString());
                }
                System.out.println("---------------");
                System.out.println(String.format("Related Tickets whose assignee_id is %s:", user.getId()));
                for (Tickets relatedTicket : lookUpTickets("assigneeId", user.getId())) {
                    System.out.println(relatedTicket.toString());
                }
                System.out.println();
            }
        }
    }

    public List<Users> lookUpUser(String term, String value) throws IOException {
        if (this.usersIndex == null) {
            //read the file of searchDataSet and build invert index
            String usersFilePath = env.getProperty("users.filepath", "users.json");
            this.users = jsonReader.parseJson(usersFilePath, Users.class);
            this.usersIndex = buildIndex(users, Users.class);
        }

        return lookUpValueFromIndex(usersIndex, users, term, value);
    }

    public List<Tickets> lookUpTickets(String term, String value) throws IOException {
        if (this.ticketsIndex == null) {
            //read the file of searchDataSet and build invert index
            String ticketsFilePath = env.getProperty("tickets.filepath", "tickets.json");
            this.tickets = jsonReader.parseJson(ticketsFilePath, Tickets.class);
            this.ticketsIndex = buildIndex(tickets, Tickets.class);
        }

        return lookUpValueFromIndex(ticketsIndex, tickets, term, value);
    }

    public List<Organizations> lookUpOrganizations(String term, String value) throws IOException {
        if (this.organizationsIndex == null) {
            //read the file of searchDataSet and build invert index
            String organizationsFilePath = env.getProperty("organizations.filepath", "organizations.json");
            this.organizations = jsonReader.parseJson(organizationsFilePath, Organizations.class);
            this.organizationsIndex = buildIndex(organizations, Organizations.class);
        }
        return lookUpValueFromIndex(organizationsIndex, organizations, term, value);
    }

    private <T extends AbstractEntity> List<T> lookUpValueFromIndex(Map<String, Map<String, List<Integer>>> index, List<T> items, String searchTerm, String searchValue) {
        List<T> lookUpResult = new ArrayList<>();
        if (index.get(searchTerm) != null && index.get(searchTerm).size() != 0) {
            List<Integer> searchValueOccurance = index.get(searchTerm).get(searchValue);
            if (searchValueOccurance != null && searchValueOccurance.size() != 0) {
                for (Integer i : searchValueOccurance) {
                    lookUpResult.add(items.get(i));
                }
            }
        }
        return lookUpResult;
    }


    private <T extends AbstractEntity> Map<String, Map<String, List<Integer>>> buildIndex(List<T> elements, Class<T> clazz) {
        Map<String, Map<String, List<Integer>>> index = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            T element = elements.get(i);
            List<Field> fields = fieldsUtil.getFieldsOfClass(clazz);
            for (Field field : fields) {
                Map<String, List<Integer>> termStat = index.getOrDefault(field.getName(), new HashMap<String, List<Integer>>());
                try {
                    Object fieldValue = FieldUtils.readField(field, element, true);
                    if (fieldValue instanceof String) {
                        List<Integer> valueOccurance = termStat.getOrDefault(fieldValue, new ArrayList<>());
                        valueOccurance.add(i);
                        termStat.put((String) fieldValue, valueOccurance);
                    } else if (fieldValue instanceof Boolean) {
                        fieldValue = fieldValue.toString();
                        List<Integer> valueOccurance = termStat.getOrDefault(fieldValue, new ArrayList<>());
                        valueOccurance.add(i);
                        termStat.put(fieldValue.toString(), valueOccurance);
                    } else if (fieldValue instanceof List) {
                        //could be tags, etc
                        for (String s : (List<String>) fieldValue) {
                            List<Integer> valueOccurance = termStat.getOrDefault(s, new ArrayList<>());
                            valueOccurance.add(i);
                            termStat.put(s, valueOccurance);
                        }
                    } else if (fieldValue == null) {
                        fieldValue = "";
                        List<Integer> valueOccurance = termStat.getOrDefault(fieldValue, new ArrayList<>());
                        valueOccurance.add(i);
                        termStat.put(fieldValue.toString(), valueOccurance);
                    }

                    index.put(field.getName(), termStat);
                } catch (Exception e) {
                    //todo: handle exception
                    e.printStackTrace();
                }
            }
        }
        return index;
    }

}
