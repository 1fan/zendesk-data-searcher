package com.zendesk.datasearcher;

import java.util.List;
import java.util.Scanner;

import com.zendesk.datasearcher.entity.Organizations;
import com.zendesk.datasearcher.entity.Tickets;
import com.zendesk.datasearcher.entity.Users;
import com.zendesk.datasearcher.processor.Processor;
import com.zendesk.datasearcher.util.ConsoleHelper;
import com.zendesk.datasearcher.util.FieldsUtil;
import org.apache.commons.text.CaseUtils;
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
            if ("1".equals(searchOption)) {
                System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
                String dataSet = scanner.nextLine();
                Class searchDataSet = null;
                List<String> fields = null;
                if ("1".equals(dataSet)) {
                    searchDataSet = Users.class;
                    fields = fieldsUtil.getFieldsInStringOfClass(Users.class);
                } else if ("2".equals(dataSet)) {
                    searchDataSet = Tickets.class;
                    fields = fieldsUtil.getFieldsInStringOfClass(Tickets.class);
                } else if ("3".equals(dataSet)) {
                    searchDataSet = Organizations.class;
                    fields = fieldsUtil.getFieldsInStringOfClass(Organizations.class);
                } else {
                    System.out.println("Please type a valid option!");
                    break;
                }

                System.out.println("Enter search term");
                String searchTerm = scanner.nextLine();
                searchTerm = CaseUtils.toCamelCase(searchTerm, false, '_');
                if (!fields.contains(searchTerm)) {
                    System.out.println("Please type a valid term!");
                } else {
                    System.out.println("Enter search value");
                    String searchValue = scanner.nextLine();
                    try {
                        if (searchDataSet.equals(Users.class)) {
                            processor.searchByUsers(searchTerm, searchValue);
                        } else if (searchDataSet.equals(Organizations.class)) {
                            processor.searchByOrganizations(searchTerm, searchValue);
                        } else if (searchDataSet.equals(Tickets.class)) {
                            processor.searchByTickets(searchTerm, searchValue);
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
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }
}
