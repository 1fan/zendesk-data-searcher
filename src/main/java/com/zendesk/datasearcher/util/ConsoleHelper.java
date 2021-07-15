package com.zendesk.datasearcher.util;

import java.util.List;
import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidInputException;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.AbstractResponse;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import com.zendesk.datasearcher.searcher.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsoleHelper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Scanner scanner = new Scanner(System.in);
    private Searcher searcher;

    @Autowired
    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
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
        for (String field : FieldUtil.getFieldNamesInStringOfClass(User.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Tickets with:");
        for (String field : FieldUtil.getFieldNamesInStringOfClass(Ticket.class)) {
            System.out.println(field);
        }
        System.out.println("---------------------------------------");
        System.out.println("Search Organizations with:");
        for (String field : FieldUtil.getFieldNamesInStringOfClass(Organization.class)) {
            System.out.println(field);
        }
    }

    /**
     * Display console instructions and handle user input to perform the search.
     *
     * @throws InvalidInputException if user input is invalid
     */
    public void handleDataSearchInput() throws InvalidInputException {
        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        Class searchDataSet = handleSearchDataSetInput();
        List<String> fields = FieldUtil.getFieldNamesInStringOfClass(searchDataSet);

        System.out.println("Enter search term");
        String searchTerm = handleSearchTermInput(searchDataSet.getSimpleName(), fields);
        String fieldName = FieldUtil.converSnakeCaseToCamelCase(searchTerm);

        System.out.println("Enter search value");
        String searchValue = scanner.nextLine();

        System.out.println();
        long start = System.currentTimeMillis();
        if (searchDataSet.equals(User.class)) {
            List<UserResponse> users = searcher.searchByUsers(fieldName, searchValue);
            printSearchResult(users, searchTerm, searchValue);
        } else if (searchDataSet.equals(Organization.class)) {
            List<OrganizationResponse> orgs = searcher.searchByOrganizations(fieldName, searchValue);
            printSearchResult(orgs, searchTerm, searchValue);
        } else if (searchDataSet.equals(Ticket.class)) {
            List<TicketResponse> tickets = searcher.searchByTickets(fieldName, searchValue);
            printSearchResult(tickets, searchTerm, searchValue);
        }
        long end = System.currentTimeMillis();
        logger.debug(String.format("Search term %s on dataset %s takes %d ms", searchTerm, searchDataSet.getSimpleName(), end - start));
    }

    private Class handleSearchDataSetInput() throws InvalidInputException {
        String dataSet = scanner.nextLine();
        Class searchDataSet;
        switch (dataSet) {
            case "1":
                searchDataSet = User.class;
                break;
            case "2":
                searchDataSet = Ticket.class;
                break;
            case "3":
                searchDataSet = Organization.class;
                break;
            default:
                throw new InvalidInputException(String.format("The option %s is not valid, please try again and select from option 1, 2 or 3.", dataSet));
        }
        return searchDataSet;
    }

    private String handleSearchTermInput(String searchDataSetName, List<String> fields) throws InvalidInputException {
        String searchTerm = scanner.nextLine();
        if (!fields.contains(searchTerm)) {
            throw new InvalidInputException(String.format("The term '%s' can't be found from dataset %s, please try again."
                    + "\n"
                    + "Or type 2 to view a list of searchable fields ", searchTerm, searchDataSetName));
        }
        return searchTerm;
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