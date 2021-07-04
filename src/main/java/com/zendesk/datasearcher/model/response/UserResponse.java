package com.zendesk.datasearcher.model.response;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;

public class UserResponse extends AbstractResponse {
    private User user;
    private Organization userOrganization;
    private List<Ticket> submittedTickets;
    private List<Ticket> assignedTickets;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(Organization userOrganization) {
        this.userOrganization = userOrganization;
    }

    public List<Ticket> getSubmittedTickets() {
        return submittedTickets;
    }

    public void setSubmittedTickets(List<Ticket> submittedTickets) {
        this.submittedTickets = submittedTickets;
    }

    public List<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(List<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    @Override
    public String toString() {
        return "# User" + "\n"
                + user.toString() + "\n" + "\n"
                + "# Related Organization" + "\n"
                + (userOrganization == null ? "NA" : userOrganization.getSummary()) + "\n" + "\n"
                + "# Related Submitted Tickets" + "\n"
                + getSummaryOfListOfEntities(submittedTickets) + "\n"
                + "# Related Assigned Tickets" + "\n"
                + getSummaryOfListOfEntities(submittedTickets) + "\n" + "\n";
    }
}
