package com.zendesk.datasearcher;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.exception.InvalidTermException;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import com.zendesk.datasearcher.processor.Processor;
import com.zendesk.datasearcher.util.ConsoleHelper;
import com.zendesk.datasearcher.util.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataSearcherCli implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Processor processor;
    private ConsoleHelper consoleHelper;
    private FieldUtil fieldUtil;

    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Autowired
    public void setConsoleHelper(ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
    }

    @Autowired
    public void setFieldUtil(FieldUtil fieldUtil) {
        this.fieldUtil = fieldUtil;
    }

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(DataSearcherCli.class, args);
    }

    @Override
    public void run(String... args) {
        consoleHelper.printWelcome();
        consoleHelper.printHelper();
        String searchOption;
        while (!"quit".equals(searchOption = scanner.nextLine().toLowerCase())) {
            try {
                switch (searchOption) {
                    case "1":
                        processDataSearch();
                        break;
                    case "2":
                        consoleHelper.printSearchableFields();
                        break;
                    default:
                        System.out.println("Invalid option selected! Please ensure select from '1' or '2'.");
                        break;
                }
            } catch (InvalidTermException | InvalidFieldException e) {
                System.out.println("Search failed: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Search failed as failed to process the JSON file, please contact Yifan for supports." + e.getMessage());
            }
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }

    private void processDataSearch() throws InvalidTermException, IOException, InvalidFieldException {
        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        Class searchDataSet = processSearchDataSetInput();
        List<String> fields = fieldUtil.getFieldNamesInStringOfClass(searchDataSet);

        System.out.println("Enter search term");
        String searchTerm = processSearchTermInput(searchDataSet.getSimpleName(), fields);
        String fieldName = fieldUtil.converSnaketToCamelNamingConvention(searchTerm);

        System.out.println("Enter search value");
        String searchValue = scanner.nextLine();

        if (searchDataSet.equals(User.class)) {
            List<UserResponse> users = processor.searchByUsers(fieldName, searchValue);
            consoleHelper.printSearchResult(users, searchTerm, searchValue);
        } else if (searchDataSet.equals(Organization.class)) {
            List<OrganizationResponse> orgs = processor.searchByOrganizations(fieldName, searchValue);
            consoleHelper.printSearchResult(orgs, searchTerm, searchValue);
        } else if (searchDataSet.equals(Ticket.class)) {
            List<TicketResponse> tickets = processor.searchByTickets(fieldName, searchValue);
            consoleHelper.printSearchResult(tickets, searchTerm, searchValue);
        }
    }

    private Class processSearchDataSetInput() throws InvalidTermException {
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
                throw new InvalidTermException(String.format("The option %s is not valid, please try again and select from option 1, 2 or 3.", dataSet));
        }
        return searchDataSet;
    }

    private String processSearchTermInput(String searchDataSetName, List<String> fields) throws InvalidTermException {
        String searchTerm = scanner.nextLine();
        if (!fields.contains(searchTerm)) {
            throw new InvalidTermException(String.format("The term '%s' can't be found from dataset %s, please try again."
                    + "\n"
                    + "Or type 2 to view a list of searchable fields ", searchTerm, searchDataSetName));
        }
        return searchTerm;
    }
}
