package com.zendesk.datasearcher.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.model.InvertedIndex;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Searcher {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private JsonFileReader jsonReader;
    private Environment env;
    private InvertedIndex<User> userInvertedIndex = null;
    private InvertedIndex<Ticket> ticketInvertedIndex = null;
    private InvertedIndex<Organization> organizationInvertedIndex = null;

    public Searcher(Environment env, JsonFileReader jsonReader) {
        this.env = env;
        this.jsonReader = jsonReader;
    }

    /**
     * Initialise the indexs.
     *
     * @throws IOException           when failed to parse JSON file
     * @throws InvalidFieldException when a field type is not supported, or failed to read a field value from the entity.
     */
    @PostConstruct
    public void initIndexes() throws IOException, InvalidFieldException {
        System.out.println("*** Loading and Processing File... ***");
        setUpUserIndex();
        setUpOrganizationIndex();
        setUpTicketIndex();
        System.out.println("*** Ready to search! ***");
    }

    private void setUpUserIndex() throws IOException, InvalidFieldException {
        String usersFilePath = env.getProperty("users.filepath", "users.json");
        List<User> users = jsonReader.parseJson(usersFilePath, User.class);
        this.userInvertedIndex = new InvertedIndex(users);
    }

    private void setUpOrganizationIndex() throws IOException, InvalidFieldException {
        String organizationsFilePath = env.getProperty("organizations.filepath", "organizations.json");
        List<Organization> organizations = jsonReader.parseJson(organizationsFilePath, Organization.class);
        this.organizationInvertedIndex = new InvertedIndex(organizations);
    }

    private void setUpTicketIndex() throws IOException, InvalidFieldException {
        String ticketFilePath = env.getProperty("tickets.filepath", "tickets.json");
        List<Ticket> tickets = jsonReader.parseJson(ticketFilePath, Ticket.class);
        this.ticketInvertedIndex = new InvertedIndex(tickets);
    }

    /**
     * Search on the Ticket dataset for the given field and given value.
     *
     * @param fieldName  the field name to search on. This field name should be consistent with the {@link Ticket} class field name.
     * @param fieldValue the value of the field name.
     * @return a List of {@link TicketResponse} that satisfy the search criteria.
     */
    public List<TicketResponse> searchByTickets(String fieldName, String fieldValue) {
        List<TicketResponse> ticketResponses = new ArrayList<>();
        List<Ticket> tickets = ticketInvertedIndex.lookUp(fieldName, fieldValue);
        if (tickets == null || tickets.size() == 0) {
            //not found
            logger.info(String.format("No available data for searched term %s with value '%s'.", fieldName, fieldValue));
        } else {
            //found
            logger.info(String.format("Found %d Tickets whose %s is '%s':", tickets.size(), fieldName, "".equals(fieldValue) ? "empty" : fieldValue));
            for (Ticket ticket : tickets) {
                TicketResponse ticketRsp = new TicketResponse();
                ticketRsp.setTicket(ticket);
                //related organization
                ticketRsp.setTicketOrganization(getOrganizationWithId(ticket.getOrganizationId()));
                //related user - assignee
                ticketRsp.setAssignee(getUserWithId(ticket.getAssigneeId()));
                //related user - submitter
                ticketRsp.setSubmitter(getUserWithId(ticket.getSubmitterId()));
                ticketResponses.add(ticketRsp);
            }
        }
        return ticketResponses;
    }

    /**
     * Search on the Organization dataset for the given field and given value.
     *
     * @param fieldName  the field name to search on. This field name should be consistent with the {@link Organization} class field name.
     * @param fieldValue the value of the field name.
     * @return a List of {@link OrganizationResponse} that satisfy the search criteria.
     */
    public List<OrganizationResponse> searchByOrganizations(String fieldName, String fieldValue) {
        List<OrganizationResponse> orgResponses = new ArrayList<>();

        List<Organization> organizations = organizationInvertedIndex.lookUp(fieldName, fieldValue);
        if (organizations == null || organizations.size() == 0) {
            //not found
            logger.info(String.format("No available data for searched term %s with value '%s'.", fieldName, fieldValue));
        } else {
            //found
            logger.info(String.format("Found %d Organizations whose %s is %s:", organizations.size(), fieldName, "".equals(fieldValue) ? "empty" : fieldValue));
            for (Organization org : organizations) {
                OrganizationResponse orgRsp = new OrganizationResponse();
                orgRsp.setOrganization(org);
                //related users
                orgRsp.setOrgUsers(userInvertedIndex.lookUp("organizationId", org.getId()));
                //related tickets
                orgRsp.setOrgTickets(ticketInvertedIndex.lookUp("organizationId", org.getId()));
                orgResponses.add(orgRsp);
            }
        }
        return orgResponses;
    }

    /**
     * Search on the User dataset for the given field and given value.
     *
     * @param fieldName  the field name to search on. This field name should be consistent with the {@link User} class field name.
     * @param fieldValue the value of the field name.
     * @return a List of {@link UserResponse} that satisfy the search criteria.
     */
    public List<UserResponse> searchByUsers(String fieldName, String fieldValue) {
        List<UserResponse> userResponses = new ArrayList<>();

        List<User> users = userInvertedIndex.lookUp(fieldName, fieldValue);
        if (users == null || users.size() == 0) {
            //not found
            logger.info(String.format("No available data for searched term %s with value '%s'.", fieldName, fieldValue));
        } else {
            //found
            logger.info(String.format("Found %d Users whose %s is %s:", users.size(), fieldName, "".equals(fieldValue) ? "empty" : fieldValue));
            for (User user : users) {
                UserResponse userResponse = new UserResponse();
                userResponse.setUser(user);
                //related org
                userResponse.setUserOrganization(getOrganizationWithId(user.getOrganizationId()));
                //related tickets - submitted by the user
                userResponse.setSubmittedTickets(ticketInvertedIndex.lookUp("submitterId", user.getId()));
                //related tickets - assigned by the user
                userResponse.setAssignedTickets(ticketInvertedIndex.lookUp("assigneeId", user.getId()));
                userResponses.add(userResponse);
            }
        }
        return userResponses;
    }

    //search User by '_id' field
    private User getUserWithId(String id) {
        List<User> results = userInvertedIndex.lookUp("id", id);
        if (results == null || results.isEmpty()) {
            return null;
        } else {
            //since we are searching by ID, there should be only 1 org
            return results.get(0);
        }
    }

    //search Organization by '_id' field
    private Organization getOrganizationWithId(String id) {
        List<Organization> orgs = organizationInvertedIndex.lookUp("id", id);
        if (orgs == null || orgs.isEmpty()) {
            return null;
        } else {
            //since we are searching by ID, there should be only 1 org
            return orgs.get(0);
        }
    }

}
