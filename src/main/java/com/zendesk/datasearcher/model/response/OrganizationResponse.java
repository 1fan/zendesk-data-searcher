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
        return "# Organization" + "\n"
                + organization.toString() + "\n" + "\n"
                + "# Related Users" + "\n"
                + getSummaryOfListOfEntities(orgUsers) + "\n"
                + "# Related Tickets" + "\n"
                + getSummaryOfListOfEntities(orgTickets) + "\n"
                + "\n";
    }
}
