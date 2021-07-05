package com.zendesk.datasearcher;

import java.io.IOException;
import java.util.Scanner;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.exception.InvalidInputException;
import com.zendesk.datasearcher.util.ConsoleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataSearcherCli implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
            } catch (InvalidFieldException e) {
                //field is not supported, or failed to read the field's value from object, we need to log it and inform the user why search failed.
                logger.warn("Search failed: ", e);
                System.out.println("Search failed: " + e.getMessage());
            } catch (IOException e) {
                //File processing failed, developer/devops need to check the files are valid.
                logger.warn("Search failed: ", e);
                System.out.println("Search failed: " + e);
                System.out.println("Please contact yifanwanghit@gmail.com for supports");
            }
            consoleHelper.printHelper();
        }
        System.exit(-1);
    }
}
