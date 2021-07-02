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
        StringBuilder userRspStr = new StringBuilder();
        userRspStr.append(ticket.toString()).append("\n")
                .append("# Related Submitter:").append("\n")
                .append(submitter == null ? "NA" : submitter.getSummary()).append("\n")
                .append("# Related Assignee:").append("\n")
                .append(assignee == null ? "NA" : assignee.getSummary()).append("\n")
                .append("# Related Organization:").append("\n")
                .append(ticketOrganization == null ? "NA" : ticketOrganization.getSummary()).append("\n");

        return userRspStr.toString();
    }
}
