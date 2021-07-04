package com.zendesk.datasearcher.util;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.AbstractResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleHelper {

    private FieldUtil fieldUtil;

    @Autowired
    public void setFieldUtil(FieldUtil fieldUtil) {
        this.fieldUtil = fieldUtil;
    }

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
        for (String field : fieldUtil.getFieldNamesInStringOfClass(User.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Tickets with:");
        for (String field : fieldUtil.getFieldNamesInStringOfClass(Ticket.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Organizations with:");
        for (String field : fieldUtil.getFieldNamesInStringOfClass(Organization.class)) {
            System.out.println(field);
        }
    }

    public <T extends AbstractResponse> void printSearchResult(List<T> results, String searchTerm, String searchValue) {
        if (results != null && results.size() > 0) {
            System.out.printf("Found %d results for term '%s' with value '%s'.\n", results.size(), searchTerm, searchValue);
            for (int i = 0; i < results.size(); i++) {
                System.out.printf("************Result %d************\n", i + 1);
                System.out.println(results.get(i).toString());
            }
        } else {
            System.out.printf("No result is found for term '%s' with value '%s'.\n", searchTerm, searchValue);
        }
    }
}