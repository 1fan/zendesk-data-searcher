package com.zendesk.datasearcher.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zendesk.datasearcher.exception.InvalidFieldException;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Searcher {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private InvertedIndex invertedIndex;

    @Autowired
    public void setInvertedIndex(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    /**
     * Search on the Ticket dataset for the given field and given value.
     *
     * @param fieldName  the field name to search on. This field name should be consistent with the {@link Ticket} class field name.
     * @param fieldValue the value of the field name.
     * @return a List of {@link TicketResponse} that satisfy the search criteria.
     * @throws IOException           when failed to parse JSON file
     * @throws InvalidFieldException when a field type is not supported, or failed to read a field value from the entity.
     */
    public List<TicketResponse> searchByTickets(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        List<TicketResponse> ticketResponses = new ArrayList<>();
        List<Ticket> tickets = invertedIndex.lookUpTickets(fieldName, fieldValue);
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
     * @throws IOException           when failed to parse JSON file
     * @throws InvalidFieldException when a field type is not supported, or failed to read a field value from the entity.
     */
    public List<OrganizationResponse> searchByOrganizations(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        List<OrganizationResponse> orgResponses = new ArrayList<>();

        List<Organization> organizations = invertedIndex.lookUpOrganizations(fieldName, fieldValue);
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
                orgRsp.setOrgUsers(invertedIndex.lookUpUser("organizationId", org.getId()));
                //related tickets
                orgRsp.setOrgTickets(invertedIndex.lookUpTickets("organizationId", org.getId()));
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
     * @throws IOException           when failed to parse JSON file
     * @throws InvalidFieldException when a field type is not supported, or failed to read a field value from the entity.
     */
    public List<UserResponse> searchByUsers(String fieldName, String fieldValue) throws IOException, InvalidFieldException {
        List<UserResponse> userResponses = new ArrayList<>();

        List<User> users = invertedIndex.lookUpUser(fieldName, fieldValue);
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
                userResponse.setSubmittedTickets(invertedIndex.lookUpTickets("submitterId", user.getId()));
                //related tickets - assigned by the user
                userResponse.setAssignedTickets(invertedIndex.lookUpTickets("assigneeId", user.getId()));
                userResponses.add(userResponse);
            }
        }
        return userResponses;
    }

    //search User by '_id' field
    private User getUserWithId(String id) throws IOException, InvalidFieldException {
        List<User> results = invertedIndex.lookUpUser("id", id);
        if (results == null || results.isEmpty()) {
            return null;
        } else {
            //since we are searching by ID, there should be only 1 org
            return results.get(0);
        }
    }

    //search Organization by '_id' field
    private Organization getOrganizationWithId(String id) throws IOException, InvalidFieldException {
        List<Organization> orgs = invertedIndex.lookUpOrganizations("id", id);
        if (orgs == null || orgs.isEmpty()) {
            return null;
        } else {
            //since we are searching by ID, there should be only 1 org
            return orgs.get(0);
        }
    }

}
