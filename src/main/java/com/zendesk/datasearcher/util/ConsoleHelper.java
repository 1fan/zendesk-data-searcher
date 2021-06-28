package com.zendesk.datasearcher.util;

import com.zendesk.datasearcher.entity.Organizations;
import com.zendesk.datasearcher.entity.Tickets;
import com.zendesk.datasearcher.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleHelper {

    @Autowired
    private FieldsUtil fieldsUtil;

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
        for (String field : fieldsUtil.getFieldsInStringOfClass(Users.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Tickets with:");
        for (String field : fieldsUtil.getFieldsInStringOfClass(Tickets.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Organizations with:");
        for (String field : fieldsUtil.getFieldsInStringOfClass(Organizations.class)) {
            System.out.println(field);
        }
    }
}
