package com.zendesk.datasearcher;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.exception.InvalidInputException;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import com.zendesk.datasearcher.processor.Searcher;
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
    private final Scanner scanner = new Scanner(System.in);
    private Searcher searcher;
    private ConsoleHelper consoleHelper;
    private FieldUtil fieldUtil;

    public static void main(String[] args) {
        SpringApplication.run(DataSearcherCli.class, args);
    }

    @Autowired
    public void setProcessor(Searcher searcher) {
        this.searcher = searcher;
    }

    @Autowired
    public void setConsoleHelper(ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
    }

    @Autowired
    public void setFieldUtil(FieldUtil fieldUtil) {
        this.fieldUtil = fieldUtil;
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
                        throw new InvalidInputException("Invalid option selected! Please ensure select from '1' or '2'.");
                }
            } catch (InvalidInputException | InvalidFieldException e) {
                logger.warn("Search failed: ", e);
                System.out.println("Search failed: " + e.getMessage());
            } catch (IOException e) {
                logger.warn("Search failed: ", e);
                System.out.println("Search failed: " + e);
                System.out.println("Please contact yifanwanghit@gmail.com for supports");
            }
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }

    private void processDataSearch() throws InvalidInputException, IOException, InvalidFieldException {
        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        Class searchDataSet = processSearchDataSetInput();
        List<String> fields = fieldUtil.getFieldNamesInStringOfClass(searchDataSet);

        System.out.println("Enter search term");
        String searchTerm = processSearchTermInput(searchDataSet.getSimpleName(), fields);
        String fieldName = fieldUtil.converSnakeCaseToCamelCase(searchTerm);

        System.out.println("Enter search value");
        String searchValue = scanner.nextLine();

        if (searchDataSet.equals(User.class)) {
            List<UserResponse> users = searcher.searchByUsers(fieldName, searchValue);
            consoleHelper.printSearchResult(users, searchTerm, searchValue);
        } else if (searchDataSet.equals(Organization.class)) {
            List<OrganizationResponse> orgs = searcher.searchByOrganizations(fieldName, searchValue);
            consoleHelper.printSearchResult(orgs, searchTerm, searchValue);
        } else if (searchDataSet.equals(Ticket.class)) {
            List<TicketResponse> tickets = searcher.searchByTickets(fieldName, searchValue);
            consoleHelper.printSearchResult(tickets, searchTerm, searchValue);
        }
    }

    private Class processSearchDataSetInput() throws InvalidInputException {
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

    private String processSearchTermInput(String searchDataSetName, List<String> fields) throws InvalidInputException {
        String searchTerm = scanner.nextLine();
        if (!fields.contains(searchTerm)) {
            throw new InvalidInputException(String.format("The term '%s' can't be found from dataset %s, please try again."
                    + "\n"
                    + "Or type 2 to view a list of searchable fields ", searchTerm, searchDataSetName));
        }
        return searchTerm;
    }
}
