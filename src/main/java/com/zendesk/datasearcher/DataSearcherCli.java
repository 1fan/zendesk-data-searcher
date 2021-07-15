package com.zendesk.datasearcher;

import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidInputException;
import com.zendesk.datasearcher.util.ConsoleHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataSearcherCli implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private ConsoleHelper consoleHelper;

    public static void main(String[] args) {
        SpringApplication.run(DataSearcherCli.class, args);
    }

    @Autowired
    public void setConsoleHelper(ConsoleHelper consoleHelper) {
        this.consoleHelper = consoleHelper;
    }

    @Override
    public void run(String... args) {
        consoleHelper.printWelcome();
        consoleHelper.printHelper();
        String searchOption;
        while (!"quit".equals(searchOption = scanner.nextLine().toLowerCase())) {
            try {
                //option 1: start search
                //option 2: show all searchable fields
                //other options: invalid and start again.
                switch (searchOption) {
                    case "1":
                        consoleHelper.handleDataSearchInput();
                        break;
                    case "2":
                        consoleHelper.printSearchableFields();
                        break;
                    default:
                        throw new InvalidInputException("Invalid option selected! Please ensure select from '1' or '2'.");
                }
            } catch (InvalidInputException e) {
                //user input is invalid, just print out the error message to inform the user why why search failed.
                System.out.println("Please check your input: " + e.getMessage());
            }
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }
}
