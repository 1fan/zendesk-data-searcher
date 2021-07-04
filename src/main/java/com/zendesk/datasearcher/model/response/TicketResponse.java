package com.zendesk.datasearcher.model.response;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;

public class TicketResponse extends AbstractResponse {
    private Ticket ticket;
    private User submitter;
    private User assignee;
    private Organization ticketOrganization;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Organization getTicketOrganization() {
        return ticketOrganization;
    }

    public void setTicketOrganization(Organization ticketOrganization) {
        this.ticketOrganization = ticketOrganization;
    }

    @Override
    public String toString() {
        return "# Ticket" + "\n"
                + ticket.toString() + "\n" + "\n"
                + "# Related Submitter" + "\n"
                + (submitter == null ? "NA" : submitter.getSummary()) + "\n" + "\n"
                + "# Related Assignee" + "\n"
                + (assignee == null ? "NA" : assignee.getSummary()) + "\n" + "\n"
                + "# Related Organization" + "\n"
                + (ticketOrganization == null ? "NA" : ticketOrganization.getSummary()) + "\n" + "\n";
    }
}
