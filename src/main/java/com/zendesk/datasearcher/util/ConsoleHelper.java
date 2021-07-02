package com.zendesk.datasearcher.util;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.AbstractResponse;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConsoleHelper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldsUtil fieldsUtil;

    @Autowired
    private Environment env;

    public void printWelcome() {
        System.out.println("Welcome to Zendesk Search");
        System.out.println("Type 'quit' to exit at any time, Press 'Enter' to continue");
    }

    public void printHelper() {
        System.out.println();
        System.out.println("******************************");
        System.out.println("Select search options:");
        System.out.println("* Press 1 to search Zendesk");
        System.out.println("* Press 2 to view a list of searchable fields");
        System.out.println("* Type 'quit' to exit");
    }

    public void printSearchableFields() {
        System.out.println("---------------------------------------");
        System.out.println("Search Users with:");
        for (String field : fieldsUtil.getFieldsInStringOfClass(User.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Tickets with:");
        for (String field : fieldsUtil.getFieldsInStringOfClass(Ticket.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Organizations with:");
        for (String field : fieldsUtil.getFieldsInStringOfClass(Organization.class)) {
            System.out.println(field);
        }
    }

    public <T extends AbstractResponse> void printSearchResult(List<T> results, String searchTerm, String searchValue) {
        if (results != null && results.size() > 0) {
            System.out.println(String.format("Found %d results for term '%s' with value '%s'.", results.size(), searchTerm, searchValue));
            for (int i = 0; i < results.size(); i++) {
                System.out.println(String.format("************Result %d************", i + 1));
                System.out.println(results.get(i).toString());
            }
        } else {
            System.out.println(String.format("No result is found for term '%s' with value '%s'.", searchTerm, searchValue));
        }
    }
}
