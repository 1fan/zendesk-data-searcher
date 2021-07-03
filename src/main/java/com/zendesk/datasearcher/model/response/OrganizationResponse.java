package com.zendesk.datasearcher.model.response;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;

public class OrganizationResponse extends AbstractResponse {
    Organization organization;

    List<User> orgUsers;
    List<Ticket> orgTickets;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<User> getOrgUsers() {
        return orgUsers;
    }

    public void setOrgUsers(List<User> orgUsers) {
        this.orgUsers = orgUsers;
    }

    public List<Ticket> getOrgTickets() {
        return orgTickets;
    }

    public void setOrgTickets(List<Ticket> orgTickets) {
        this.orgTickets = orgTickets;
    }

    @Override
    public String toString() {
        StringBuilder userRspStr = new StringBuilder();
        userRspStr.append(organization.toString()).append("\n").append("\n")
                .append("# Related Users:").append("\n")
                .append(getSummaryOfListOfEntities(orgUsers)).append("\n")
                .append("# Related Tickets:").append("\n")
                .append(getSummaryOfListOfEntities(orgTickets)).append("\n")
                .append("\n");

        return userRspStr.toString();
    }
}
