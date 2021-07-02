package com.zendesk.datasearcher;

import java.util.List;
import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidTermException;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import com.zendesk.datasearcher.processor.Processor;
import com.zendesk.datasearcher.util.ConsoleHelper;
import com.zendesk.datasearcher.util.FieldsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataSearcherCli implements CommandLineRunner {

    @Autowired
    private Processor processor;

    @Autowired
    private ConsoleHelper consoleHelper;

    @Autowired
    private FieldsUtil fieldsUtil;

    private Scanner scanner = new Scanner(System.in);

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
                if ("1".equals(searchOption)) {
                    System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
                    String dataSet = scanner.nextLine();
                    Class searchDataSet;
                    List<String> fields;
                    switch (dataSet) {
                        case "1":
                            searchDataSet = User.class;
                            fields = fieldsUtil.getFieldsInStringOfClass(User.class);
                            break;
                        case "2":
                            searchDataSet = Ticket.class;
                            fields = fieldsUtil.getFieldsInStringOfClass(Ticket.class);
                            break;
                        case "3":
                            searchDataSet = Organization.class;
                            fields = fieldsUtil.getFieldsInStringOfClass(Organization.class);
                            break;
                        default:
                            throw new InvalidTermException(String.format("The option %s is not valid, please try again and select from option 1, 2 or 3.", dataSet));
                    }

                    System.out.println("Enter search term");
                    String searchTerm = scanner.nextLine();
                    if (!fields.contains(searchTerm)) {
                        throw new InvalidTermException(String.format("The term '%s' can't be found from dataset %s, please try again."
                                + "\n"
                                + "Or type 2 to view a list of searchable fields ", searchTerm, searchDataSet.getSimpleName()));
                    } else {
                        System.out.println("Enter search value");
                        String searchValue = scanner.nextLine();
                        try {
                            if (searchDataSet.equals(User.class)) {
                                List<UserResponse> users = processor.searchByUsers(searchTerm, searchValue);
                                consoleHelper.printSearchResult(users, searchTerm, searchValue);
                            } else if (searchDataSet.equals(Organization.class)) {
                                List<OrganizationResponse> orgs = processor.searchByOrganizations(searchTerm, searchValue);
                                consoleHelper.printSearchResult(orgs, searchTerm, searchValue);
                            } else if (searchDataSet.equals(Ticket.class)) {
                                List<TicketResponse> tickets = processor.searchByTickets(searchTerm, searchValue);
                                consoleHelper.printSearchResult(tickets, searchTerm, searchValue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if ("2".equals(searchOption)) {
                    consoleHelper.printSearchableFields();
                } else {
                    System.out.println("Invalid option selected!");
                }
            } catch (InvalidTermException e) {
                System.out.println(e.getMessage());
            }
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }
}
