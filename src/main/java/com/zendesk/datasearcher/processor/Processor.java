package com.zendesk.datasearcher.processor;

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
public class Processor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InvertedIndex invertedIndex;

    public List<TicketResponse> searchByTickets(String searchTerm, String searchValue) throws IOException, InvalidFieldException {
        List<TicketResponse> ticketResponses = new ArrayList<>();
        List<Ticket> tickets = invertedIndex.lookUpTickets(searchTerm, searchValue);
        if (tickets == null || tickets.size() == 0) {
            //not found
            logger.info(String.format("No available data for searched term %s with value '%s'.", searchTerm, searchValue));
        } else {
            //found
            logger.info(String.format("Found %d Tickets whose %s is '%s':", tickets.size(), searchTerm, "".equals(searchValue) ? "empty" : searchValue));
            for (Ticket ticket : tickets) {
                TicketResponse ticketRsp = new TicketResponse();
                ticketRsp.setTicket(ticket);
                List<Organization> orgs = invertedIndex.lookUpOrganizations("id", ticket.getOrganizationId());
                if (orgs != null) {
                    //since we are searching by ID, there should be only 1 org
                    ticketRsp.setTicketOrganization(orgs.get(0));
                }
                List<User> assignees = invertedIndex.lookUpUser("id", ticket.getAssigneeId());
                if (assignees != null) {
                    //since we are searching by ID, there should be only 1 org
                    ticketRsp.setAssignee(assignees.get(0));
                }

                List<User> submitters = invertedIndex.lookUpUser("id", ticket.getSubmitterId());
                if (submitters != null) {
                    ticketRsp.setSubmitter(submitters.get(0));
                }
                ticketResponses.add(ticketRsp);
            }
        }
        return ticketResponses;
    }

    public List<OrganizationResponse> searchByOrganizations(String searchTerm, String searchValue) throws IOException, InvalidFieldException {
        List<OrganizationResponse> orgResponses = new ArrayList<>();

        List<Organization> organizations = invertedIndex.lookUpOrganizations(searchTerm, searchValue);
        if (organizations == null || organizations.size() == 0) {
            //not found
            logger.info(String.format("No available data for searched term %s with value '%s'.", searchTerm, searchValue));
        } else {
            //found
            logger.info(String.format("Found %d Organizations whose %s is %s:", organizations.size(), searchTerm, "".equals(searchValue) ? "empty" : searchValue));
            for (Organization org : organizations) {
                OrganizationResponse orgRsp = new OrganizationResponse();
                orgRsp.setOrganization(org);
                orgRsp.setOrgUsers(invertedIndex.lookUpUser("organizationId", org.getId()));
                orgRsp.setOrgTickets(invertedIndex.lookUpTickets("organizationId", org.getId()));
                orgResponses.add(orgRsp);
            }
        }
        return orgResponses;
    }

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
                List<Organization> relatedOrg = invertedIndex.lookUpOrganizations("id", user.getOrganizationId());
                if (relatedOrg != null) {
                    //since we are searching on ID, there should be only 1 or 0 organization.
                    userResponse.setUserOrganization(relatedOrg.get(0));
                }
                userResponse.setSubmittedTickets(invertedIndex.lookUpTickets("submitterId", user.getId()));
                userResponse.setAssignedTickets(invertedIndex.lookUpTickets("assigneeId", user.getId()));
                userResponses.add(userResponse);
            }
        }
        return userResponses;
    }

}
